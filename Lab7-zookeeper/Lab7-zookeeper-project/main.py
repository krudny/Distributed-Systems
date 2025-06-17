import sys
import os
import time
import subprocess
from kazoo.client import KazooClient
from kazoo.exceptions import NoNodeError
from kazoo.protocol.states import EventType


class ZookeeperTerminalMonitor:
    # Połączenie z zookeeperem za pomocą klienta kazoo
    def __init__(self, executable_path, zk_connection):
        self.executable_path = executable_path
        self.zk_hosts = zk_connection
        self.process = None

        self.zk = KazooClient(hosts=self.zk_hosts)
        self.zk.start()

        self.setup_watcher()

    # Watcher na znode a
    def setup_watcher(self):
        @self.zk.DataWatch('/a')
        def watch_node(data, stat, event):
            if event:
                self.handle_event(event)

    # uruchomienie aplikacji graficznej
    def handle_event(self, event):
        if event.type == EventType.CREATED:
            print("[ZK] Node '/a' created.")
            self.launch_app()
        elif event.type == EventType.DELETED:
            print("[ZK] Node '/a' deleted.")
            self.terminate_app()

    def launch_app(self):
        if self.process is None:
            self.process = subprocess.Popen([self.executable_path])
            print("[APP] Launched.")

    def terminate_app(self):
        if self.process:
            self.process.terminate()
            self.process = None
            print("[APP] Terminated.")

    # wyswietlanie drzewa
    def display_tree(self, path, level=0):
        try:
            children = self.zk.get_children(path)
            indent = "  " * level
            output = f"{indent}{os.path.basename(path) or '/'}\n"
            for child in children:
                full_path = f"{path}/{child}" if path != "/" else f"/{child}"
                output += self.display_tree(full_path, level + 1)
            return output
        except NoNodeError:
            return f"[!] Node '{path}' does not exist.\n"

    def count_children(self, path='/a'):
        try:
            children = self.zk.get_children(path)
            return len(children)
        except NoNodeError:
            return 0

    def run(self):
        try:
            while True:
                print("=" * 40)
                print(self.display_tree('/a'))
                print(f"[ZK] Number of children in '/a': {self.count_children()}")
                print("=" * 40)
                time.sleep(5)
        except KeyboardInterrupt:
            print("\n[INFO] Exiting...")
            self.cleanup()

    def cleanup(self):
        self.terminate_app()
        self.zk.stop()
        self.zk.close()


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python main.py <app_executable_path> <zookeeper_port>")
        sys.exit(1)

    app_path = sys.argv[1]
    zk_port = sys.argv[2]
    zk_hosts = f"127.0.0.1:{zk_port}"

    monitor = ZookeeperTerminalMonitor(app_path, zk_hosts)
    monitor.run()
