from traitlets.config import get_config

c = get_config()
# Wyłączenie tokena i ciastek autoryzacyjnych
c.ServerApp.token = ''
c.ServerApp.password = ''

# Pozwolenie na zdalny dostęp i WebSockety z dowolnego hosta
c.ServerApp.allow_remote_access = True
c.ServerApp.allow_origin = '*'
