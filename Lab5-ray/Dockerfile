FROM python:3.10-slim

RUN apt-get update && apt-get install -y \
    curl \
    git \
    build-essential \
    && rm -rf /var/lib/apt/lists/*


RUN pip install --no-cache-dir \
    jupyterlab \
    ipykernel \
    ray[default] \
    ipywidgets \ 
    numpy \
    tqdm 

COPY jupyter_lab_config.py /root/.jupyter/jupyter_lab_config.py

WORKDIR /workspace

EXPOSE 8888 8265

CMD ["bash", "-c", \
     "ray start --head --dashboard-host=0.0.0.0 && \
      jupyter lab --ip=0.0.0.0 --port=8888 --no-browser --allow-root"]