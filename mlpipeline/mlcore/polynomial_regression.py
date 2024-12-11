from abc import abstractmethod
from matplotlib import pyplot as plt
import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import PolynomialFeatures
from mlcore.ml_model import MlModel
from sklearn.metrics import roc_auc_score, recall_score, precision_score, f1_score, cohen_kappa_score, matthews_corrcoef, accuracy_score, \
    mean_squared_error, r2_score, mean_absolute_error
from sklearn.preprocessing import LabelBinarizer

from config import PolynomialRegression as PolynomialRegressionConfig

class PolynomialRegressionModel(MlModel):
    _name = "PolynomialRegression"
    _model: LogisticRegression

    def __init__(self) -> None:
        self._model = LogisticRegression()
        self._polynomal_features = PolynomialFeatures(PolynomialRegressionConfig.get('degree'))

    def train(self, x, y):
        x_poly = self._polynomal_features.fit_transform(x)
        self._model.fit(x_poly, y)

    def test(self, x, y):
        x_poly = self._polynomal_features.fit_transform(x)
        results = self._model.predict(x_poly)
        results_shaped = results.flatten().reshape(-1, 1)

        results_proba = self._model.predict_proba(x_poly)[:, 1]

        lb = LabelBinarizer()
        y_bin = lb.fit_transform(y)

        self._accuracy_score = accuracy_score(y, results)
        self._mse = mean_squared_error(y, results)
        self._mae = mean_absolute_error(y, results)
        self._r2 = r2_score(y, results)

        self._auc = roc_auc_score(y_bin, results_shaped, multi_class='ovr')
        self._recall = recall_score(y, results_shaped, average='micro')
        self._precision = precision_score(y, results_shaped, average='micro')
        self._f1 = f1_score(y, results_shaped, average='micro')
        self._kappa = cohen_kappa_score(y, results_shaped)
        self._mcc = matthews_corrcoef(y, results_shaped)
