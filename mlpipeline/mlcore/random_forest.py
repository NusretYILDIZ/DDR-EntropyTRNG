from abc import abstractmethod
from matplotlib import pyplot as plt
import pandas as pd
import numpy as np
from sklearn.discriminant_analysis import StandardScaler
from sklearn.metrics import accuracy_score, mean_absolute_error, mean_squared_error

from mlcore.ml_model import MlModel
from sklearn.ensemble import RandomForestClassifier 
from sklearn.metrics import roc_auc_score, recall_score, precision_score, f1_score, cohen_kappa_score, matthews_corrcoef, accuracy_score, \
    mean_squared_error, r2_score, mean_absolute_error
from sklearn.preprocessing import LabelBinarizer
from config import RandomForest as RandomForestConfig

class RandomForestModel(MlModel):
    _name = "RandomForest"
    _model: RandomForestClassifier
    _scaler: StandardScaler

    def __init__(self) -> None:
        self._model = RandomForestClassifier(n_estimators=RandomForestConfig.get("n_estimators"), random_state=RandomForestConfig.get("random_state"))

    def train(self, x, y):
        self._scaler = StandardScaler()
        self._scaler.fit(x)
        self._model.fit(self._scaler.transform(x), y)
    
    def test(self, x, y):
        x_scaled = self._scaler.transform(x)
        results = self._model.predict(x_scaled)
        results_shaped = results.flatten().reshape(-1, 1)

        lb = LabelBinarizer()
        y_bin = lb.fit_transform(y)
        
        self._accuracy_score = self._calculate_accuracy(y, results, RandomForestConfig.get('fault_tolarence'))
        self._mse = mean_squared_error(y, results)
        self._mae = mean_absolute_error(y, results)
        self._r2 = r2_score(y, results)

        self._auc = roc_auc_score(y_bin, results_shaped, multi_class='ovr')
        self._recall = recall_score(y, results_shaped, average='micro')
        self._precision = precision_score(y, results_shaped, average='micro')
        self._f1 = f1_score(y, results_shaped, average='micro')
        self._kappa = cohen_kappa_score(y, results_shaped)
        self._mcc = matthews_corrcoef(y, results_shaped)
    


    def _calculate_accuracy(self, expected, predicted, tolerance):
        within_tolerance = np.abs(expected - predicted) <= tolerance
        accuracy = np.mean(within_tolerance)
        return accuracy
