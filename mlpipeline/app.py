from pick import pick
from tqdm import tqdm

import mlcore
from mlcore import MODELS, MlCore
from utils.dataset import DataBuilder

data_builder = DataBuilder("./_test/data_test.csv")

for index, row in tqdm(data_builder.dataset.iterrows(), total=data_builder.dataset.shape[0], desc="Processing CSV"):
    raw_value = str(row['output']).strip()
    decimal_value = int(raw_value.zfill(8), 2)
    data_builder.dataset.at[index, 'output'] = decimal_value

# [1/5] Choose column indexes to drop [1,2,3, ...] etc
data_builder.drop_unrelated_columns([])

# [2/5] Choose column as output label by its name
data_builder.select_output_column("output")

# [3/5] Set test data/entire data ratio (Between 0-1)
data_builder.split_test_ratio(0.5)

# [4/5] Selecting Machine learning models
selection_result = pick(
    list(mlcore.MODELS.keys()),
    "[4/5] Select Machine learning models to attack\n(press SPACE to mark, ENTER to continue)", 
    multiselect=True, 
    min_selection_count=1
)

selected_models = [item[0] for item in selection_result]

pipeline = MlCore() \
    .use_data_builder(data_builder) \
    .use_models(selected_models) \
    .export_path("/_output/metrics.txt")

results = pipeline.run()
