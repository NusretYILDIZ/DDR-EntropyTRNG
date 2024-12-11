from mlcore.lstm_neural_networks import LSTMNeuralNetworkModel
from mlcore.classical_neural_networks import NeuralNetworkModel
from mlcore.desicion_trees import DecisionTreeModel
from mlcore.k_means import KmeansModel
from mlcore.linear_svm import LinearSvmModel
from mlcore.logistic_regression import LogisticRegressionModel
from mlcore.polynomial_regression import PolynomialRegressionModel
from mlcore.polynomial_svm import PolynomialSvmModel
from mlcore.radial_svm import RadialSvmModel
from mlcore.random_forest import RandomForestModel

from mlcore.ml_model import MlModel
from utils.dataset import DataBuilder

from tabulate import tabulate

MODELS = {
    "PolynomialRegression": PolynomialRegressionModel,
    "LogisticRegression": LogisticRegressionModel,
    "DecisionTree": DecisionTreeModel,
    "RandomForest": RandomForestModel,
    "RadialSvm": RadialSvmModel,
    "PolynomialSvm": PolynomialSvmModel,
    "LinearSvm": LinearSvmModel,
    "Kmeans": KmeansModel,
    "NeuralNetwork": NeuralNetworkModel,
    "LSTMNeuralNetworkModel": LSTMNeuralNetworkModel
}

class MlCore:
    __models: list[MlModel] = []
    __data_builder:DataBuilder
    __export_path:str

    def use_data_builder(self, data_builder:DataBuilder):
        self.__data_builder = data_builder
        return self

    def use_models(self, models: list[str]):
        for model_name in models:
            new_model_instance = MODELS[model_name]()
            self.__models.append(new_model_instance)

        return self

    def export_path(self, export_path:str):
        self.__export_path = export_path

        return self

    def run(self):
        scores = {}
        for each_ml_model in self.__models:
            [x_train, y_train] =  self.__data_builder.get_train_data()
            print(f"=> Training started for: {each_ml_model.get_name()}")
            each_ml_model.train(x_train, y_train)
            print(f"=> Training end: {each_ml_model.get_name()}")

            [x_test, y_test] = self.__data_builder.get_test_data()
            each_ml_model.test(x_test, y_test)

            scores[each_ml_model.get_name()] = {
                "accuracy score": each_ml_model.get_accuracy_score(),
                "mean square error": each_ml_model.get_mse(),
                "mean absolute error": each_ml_model.get_mae(),
                "r2 score": each_ml_model.get_r2(),

                "AUC": each_ml_model.get_auc(),
                "RECALL": each_ml_model.get_recall(),
                "Precision": each_ml_model.get_precision(),
                "F1": each_ml_model.get_f1(),
                "Kappa": each_ml_model.get_kappa(),
                "MCC": each_ml_model.get_mcc(),
            }

        table_data = []
        for model_name, scores in scores.items():
            row = [model_name]
            row.extend(scores.values())
            table_data.append(row)

        headers = ["Model", "Accuracy Score", "Mean Square Error", "Mean Absolute Error", "R2 Score",\
            "AUC", "RECALL", "Precision", "F1", "Kappa", "MCC"
        ]
        print(tabulate(table_data, headers=headers, tablefmt="grid"))
