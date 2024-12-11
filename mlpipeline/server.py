from flask import Flask, request, render_template
from flask_cors import CORS, cross_origin

app = Flask(__name__)
app.config['CORS_HEADERS'] = 'Content-Type'
cors = CORS(app)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=80, debug=True, threaded=True)