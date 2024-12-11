from abc import abstractmethod
from matplotlib import pyplot as plt

from tensorflow import keras
from keras.models import Sequential
from keras.layers import LSTM, Dense
from keras.optimizers import Adam
from sklearn.metrics import roc_auc_score, recall_score, precision_score, f1_score, cohen_kappa_score, matthews_corrcoef, accuracy_score, \
    mean_squared_error, r2_score, mean_absolute_error

from mlcore.ml_model import MlModel
from sklearn.preprocessing import LabelBinarizer

from config import LSTMNeuralNetwork as LSTMNeuralNetworkConfig
import os

os.environ["CUDA_VISIBLE_DEVICES"] = ""

class LSTMNeuralNetworkModel(MlModel):
    _name = "LSTMNeuralNetwork"
    _model: Sequential

    def __init__(self):
        input_shape = LSTMNeuralNetworkConfig.get("input_shape")
        hidden_layers = LSTMNeuralNetworkConfig.get("hidden_layers")
        learning_rate = LSTMNeuralNetworkConfig.get("learning_rate")
        activation = LSTMNeuralNetworkConfig.get("activation")
        output_activation = LSTMNeuralNetworkConfig.get("output_activation")
        loss_function = LSTMNeuralNetworkConfig.get("loss_function")

        self._model = Sequential()
        self._model.add(LSTM(hidden_layers[0], input_shape=input_shape, activation=activation, recurrent_activation='sigmoid', return_sequences=len(hidden_layers) > 1))
        for units in hidden_layers[1:-1]:
            self._model.add(LSTM(units, activation=activation, return_sequences=True))
        self._model.add(LSTM(hidden_layers[-1], activation=activation))
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
