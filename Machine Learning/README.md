# Machine Learning

The model we use for machine learning is YOLOv5, which is a transfer learning model that can be used for easier model creation by leveraging existing layers to enhance performance. Here is the dataset we use for training: Fruit Vegetable Dataset [Fruit Vegetable Dataset](https://www.kaggle.com/datasets/kritikseth/fruit-and-vegetable-image-recognition?datasetId=952827&sortBy=voteCount). But, we don't use all of classes, only:
- 0: grape
- 1: apple
- 2: banana
- 3: carrot
- 4: cucumber
- 5: tomato
- 6: broccoli
- 7: pineaple
- 8: orange
- 9: selada
- 10: avocado
- 11: cabbage
- 12: cauliflower
- 13: eggplant
- 14: kiwi
- 15: potato
- 16: strawberry

## Important link

- [Capstone notebook](https://colab.research.google.com/drive/1833wzCXwM5f0c1tSlJaB0WY2whqh5hPs?authuser=3)

- [Labellig dataset](https://drive.google.com/drive/folders/1NrOvMXUZT56A8xWUOczoUBmby_rI0UEH?usp=sharing)
- [YOLO Format labels](https://drive.google.com/drive/folders/1B1rTCfnVJkpy66FIBa41xuEoavyiXQor?usp=sharing)

## Prerequisites
1. [Jupyter Notebook](https://test-jupyter.readthedocs.io/en/latest/install.html) or [Google Colab](https://colab.research.google.com/)
3. [Python](https://www.python.org/downloads/) version 3.6 or above
4. Start from a Python>=3.8 environment with PyTorch>=1.7 installed. To install PyTorch see [this](https://pytorch.org/get-started/locally/). To install YOLOv5 dependencies:
