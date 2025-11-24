from textblob import TextBlob
from flask import Flask, request, jsonify
from healthcheck import HealthCheck

app = Flask(__name__)
health = HealthCheck()

@app.route("/analyse/sentence", methods=['POST'])
def analyse_sentence():
    app.logger.info('Received request: ' + request.get_data(as_text=True))
    sentence = request.get_json()['sentence']
    polarity = TextBlob(sentence).sentences[0].polarity
    app.logger.info('Processed sentence: [{}] returning polarity [{}]'.format(sentence, polarity))
    return jsonify(
        sentence=sentence,
        polarity=polarity
    )

app.add_url_rule("/health", "healthcheck", view_func=lambda: health.run())

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
