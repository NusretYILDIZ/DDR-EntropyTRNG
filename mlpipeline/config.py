PolynomialRegression = {
    "degree": 3
}

DecisionTree = {
    "criterion": "entropy",
    "random_state": 0
}

RandomForest = {
    "n_estimators": 2, 
    "random_state": 0,
    "fault_tolarence": 0.5
}

PolynomialRegression = {
    "degree": 2
}

RadialSvm = {
    "C": 10, 
    "gamma": 0.001, 
    "kernel": "rbf"
}

Kmeans = {
    "n_clusters":10, 
    "random_state":0
}

NeuralNetwork = {
    "input_dim": 24,
    "hidden_layers": [64, 64],
    "learning_rate": 0.001,
    "activation": "relu",
    "output_activation": "sigmoid",
    "loss_function": "binary_crossentropy"
}

LSTMNeuralNetwork = {
     "input_shape": (24, 1),
     "hidden_layers": [64, 64, 32],
     "learning_rate": 0.001,
     "activation": 'sigmoid',
     "output_activation": 'sigmoid',
     "loss_function": 'binary_crossentropy'
}