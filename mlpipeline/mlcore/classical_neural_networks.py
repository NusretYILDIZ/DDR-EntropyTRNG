from abc import abstractmethod
from matplotlib import pyplot as plt

from tensorflow import keras
from keras.models import Sequential
from keras.layers import Dense
from keras.optimizers import Adam
from sklearn.metrics import roc_auc_score, recall_score, precision_score, f1_score, cohen_kappa_score, matthews_corrcoef, accuracy_score, \
    mean_squared_error, r2_score, mean_absolute_error

from mlcore.ml_model import MlModel
from sklearn.preprocessing import LabelBinarizer

from config import NeuralNetwork as NeuralNetworkConfig

class NeuralNetworkModel(MlModel):
    _name = "NeuralNetwork"
    _model: Sequential

    def __init__(self):
        input_dim = NeuralNetworkConfig.get("input_dim")
        hidden_layers = NeuralNetworkConfig.get("hidden_layers")
        learning_rate = NeuralNetworkConfig.get("learning_rate")
        activation = NeuralNetworkConfig.get("activation")
        output_activation = NeuralNetworkConfig.get("output_activation")
        loss_function = NeuralNetworkConfig.get("loss_function")

        self._model = Sequential()
        self._model.add(Dense(hidden_layers[0], input_dim=input_dim, activation=activation))
        for units in hidden_layers[1:]:
            self._model.add(Dense(units, activation=activation))
        self._model.add(Dense(1, activation=output_activation))

        self._model.compile(optimizer=Adam(learning_rate=learning_rate), loss=loss_function, metrics=['accuracy'])

    def train(self, x, y, epochs=2, batch_size=32, verbose=1):
        self._model.fit(x, y, epochs=epochs, batch_size=batch_size, verbose=verbose)

    def test(self, x, y):
        results = self._model.predict(x).flatten()
        results = results.reshape(-1, 1)

        lb = LabelBinarizer()
        y_bin = lb.fit_transform(y)
        
        self._accuracy_score = accuracy_score(y, results)
        self._mse = mean_squared_error(y, results)
        self._mae = mean_absolute_error(y, results)
        self._r2 = r2_score(y, results)

        self._auc = roc_auc_score(y_bin, results, multi_class='ovr')
        self._recall = recall_score(y, results, average='micro')
        self._precision = precision_score(y, results, average='micro')
        self._f1 = f1_score(y, results, average='micro')
        self._kappa = cohen_kappa_score(y, results)
        self._mcc = matthews_corrcoef(y, results)

#auc, recall, precision, f1, kappa, mcc