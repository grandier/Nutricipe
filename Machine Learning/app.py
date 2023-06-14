import argparse
import io
import requests
from PIL import Image

import torch
from flask import Flask, request, jsonify

app = Flask(__name__)

URL = "/yolo/predict"

model = None
confidence_threshold = 0.6
@app.route(URL, methods=["POST"])
def predict():
    global model
    global model2
    labels = set()
    my_labels = ["grape", "apple", "banana", "carrot", "cucumber", "tomato", "broccoli", "pineaple", "orange", "selada", "avocado", "cabbage","cauliflower","eggplant","kiwi","potato","strawberry"]
    if not request.method == "POST":
        return

    if request.form.get("url"):
        image_url = request.form["url"]
        response = requests.get(image_url)
        image_bytes = response.content
        img = Image.open(io.BytesIO(image_bytes))

        if model is not None or model1 is not None or model2 is not None:
            results = model(img, size=640)
            results2 = model2(img, size=640)
            filtered_results = results.pandas().xyxy[0][results.pandas().xyxy[0]['confidence'] >= confidence_threshold]
            filtered_results2 = results2.pandas().xyxy[0][results2.pandas().xyxy[0]['confidence'] >= confidence_threshold]
            for result in filtered_results["name"]:
                if result in my_labels:
                    labels.add(result)
            for result2 in filtered_results2["name"]:
                if result2 in my_labels:
                    labels.add(result2)
            return jsonify(list(labels))
        else:
            return jsonify({"error": "Model not loaded. Please check the model initialization."})

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Flask API exposing YOLOv5 model")
    parser.add_argument("--port", default=5000, type=int, help="port number")
    parser.add_argument('--model', default='yolov5s', help='model to run, i.e. --model yolov5s')
    args = parser.parse_args()


    model = torch.hub.load('ultralytics/yolov5', 'custom', path='best2')
    model2 = torch.hub.load('ultralytics/yolov5', 'custom', path='best_3')
    app.run(host="0.0.0.0", port=args.port)