FROM python:3.8-slim-buster

COPY api /app
WORKDIR /app

RUN pip3 install -r requirements.txt

RUN python3 -m textblob.download_corpora

EXPOSE 5000
ENTRYPOINT ["python3"]
CMD ["logic_server.py"]