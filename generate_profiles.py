import os

# Set your target folder (relative to your Git repo root)
folder_name = "customer-profile-dataset"
os.makedirs(folder_name, exist_ok=True)

# Configurable settings
num_files = 10
lines_per_file = 10

# Generate the CSV files
for i in range(1, num_files + 1):
    file_path = os.path.join(folder_name, f"profile_data_{i}.csv")
    with open(file_path, "w") as f:
        for j in range(lines_per_file):
            profile_id = f"profile_{i}_{j:04d}"
            f.write(f"{profile_id}\n")

print(f"{num_files} files created in '{folder_name}'")
