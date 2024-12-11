from abc import abstractmethod
from matplotlib import pyplot as plt
import pandas as pd
import numpy as np
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import roc_auc_score, recall_score, precision_score, f1_score, cohen_kappa_score, matthews_corrcoef, accuracy_score, \
    mean_squared_error, r2_score, mean_absolute_error
from sklearn.preprocessing import LabelBinarizer
from mlcore.ml_model import MlModel
from config import Kmeans as KmeansConfig

class KmeansModel(MlModel):
    _name = "Kmeans"
    _model: KMeans

    def __init__(self) -> None:
        self._model = KMeans(n_clusters=KmeansConfig.get("n_clusters"), init='k-means++', random_state=KmeansConfig.get("random_state"))

    def train(self, x, y):
        self._scaler = StandardScaler()
        self._scaler.fit(x)
        self._model.fit(self._scaler.transform(x), y)

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