from abc import ABC, abstractmethod
from sklearn.linear_model import LinearRegression
from sklearn.metrics import (accuracy_score, roc_auc_score, recall_score, 
                             precision_score, f1_score, cohen_kappa_score, 
                             matthews_corrcoef, mean_squared_error)

class MlModel(ABC):
    def __init__(self):
        self._name = None
        self._accuracy_score = 0
        self._mse = 0
        self._mae = 0
        self._r2 = 0
        self._auc = 0
        self._recall = 0
        self._precision = 0
        self._f1 = 0
        self._kappa = 0
        self._mcc = 0
    
    @abstractmethod
    def train(self, x, y):
        pass

    @abstractmethod
    def test(self, x, y):
        pass

    def get_name(self):
        return self._name

    def get_accuracy_score(self):
        return self._accuracy_score

    def get_mse(self):
        return self._mse

    def get_mae(self):
        return self._mae

    def get_r2(self):
        return self._r2

    def get_auc(self):
        return self._auc

    def get_recall(self):
        return self._recall

    def get_precision(self):
        return self._precision

    def get_f1(self):
        return self._f1

    def get_kappa(self):
        return self._kappa

    def get_mcc(self):
        return self._mcc