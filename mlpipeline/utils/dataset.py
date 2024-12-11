import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split

""" Builder Pattern """
class DataBuilder:
    dataset:pd.DataFrame
    __output_label_index:int
    __x_train: pd.DataFrame
    __x_test: pd.DataFrame
    __y_train: pd.DataFrame
    __y_test: pd.DataFrame
    
    def __init__(self, path):
        try:
            self.dataset = pd.read_csv(path)
            self.dataset.drop_duplicates(inplace=True)
            self.dataset.dropna(inplace=True)
            if self.dataset.empty:
                print("Error: The dataset is empty after removing duplicates and NaN values.")
                self.dataset = None
        except Exception as e:
            print(f"An error occurred while loading the dataset: {e}")
            self.dataset = None
        
    def get_train_data(self):
        return [self.__x_train, self.__y_train]

    def get_test_data(self):
         return [self.__x_test, self.__y_test]
   
    def drop_unrelated_columns(self, columns_to_drop: list[str]):
        invalid_columns = [col for col in columns_to_drop if col not in self.dataset.columns]
        assert len(invalid_columns) == 0, f"Error: The following columns do not exist in the dataset and cannot be dropped: {invalid_columns}"

        self.dataset.drop(columns=columns_to_drop, inplace=True)

    def select_output_column(self, column_name):
        assert self.dataset is not None, "Error: Dataset not loaded."
        assert column_name in self.dataset.columns, f"Error: Column '{column_name}' does not exist in the dataset."
        
        self.__output_label_index = self.dataset.columns.get_loc(column_name)

    def split_test_ratio(self, test_size):
        if self.dataset is None:
            print("Error: Dataset not loaded.")
            return None, None
        
        if self.__output_label_index == None:
            print("Error: Output label index not set.")
            return None, None

        features = self.dataset.drop(columns=[self.dataset.columns[self.__output_label_index]])
        labels = self.dataset.iloc[:, self.__output_label_index]

        if features.empty or labels.empty:
            print("Error: Features or labels are empty.")
            return None, None

        try:
            self.__x_train, self.__x_test, self.__y_train, self.__y_test = train_test_split(features, labels, test_size=test_size, random_state=42)
        except ValueError as e:
            print(f"An error occurred during train-test split: {e}")
            return None, None