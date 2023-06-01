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
    global model1
    labels = set()
    my_labels = ["grape", "apple", "banana", "carrot", "cucumber", "tomato", "broccoli", "pineaple", "orange", "selada"]
    if not request.method == "POST":
        return

    if request.files.get("image"):
        image_file = request.files["image"]
        image_bytes = image_file.read()
        img = Image.open(io.BytesIO(image_bytes))

        if model is not None and model1 is not None:
            results = model(img, size=640)
            results1 = model1(img, size=640)
            filtered_results = results.pandas().xyxy[0][results.pandas().xyxy[0]['confidence'] >= confidence_threshold]
            filtered_results1 = results1.pandas().xyxy[0][results1.pandas().xyxy[0]['confidence'] >= confidence_threshold]
            for result in filtered_results["name"]:
                if result in my_labels:
                    labels.add(result)
            for result1 in filtered_results1["name"]:
                if result1 in my_labels:
                    labels.add(result1)

            return jsonify(success= True, data=list(labels))
        else:
            return jsonify({"error": "Model not loaded. Please check the model initialization."})

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Flask API exposing YOLOv5 model")
    parser.add_argument("--port", default=8080, type=int, help="port number")
    parser.add_argument('--model', default='yolov5s', help='model to run, i.e. --model yolov5s')
    args = parser.parse_args()


    model = torch.hub.load('ultralytics/yolov5', 'custom', path='best2')
    model1 = torch.hub.load('ultralytics/yolov5', 'custom', path='best')
    app.run(host="0.0.0.0", port=args.port)