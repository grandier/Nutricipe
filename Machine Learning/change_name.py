import os

folder_path = r"C:\Users\fadel\Documents\Capstone\Dataset\train\orange"  # Ganti dengan path direktori/folder yang sesuai

# Mendapatkan daftar file dalam folder
files = os.listdir(folder_path)

# Iterasi melalui setiap file
for file in files:
    if file.startswith("Image_") and file.endswith(".png"):
        file_path = os.path.join(folder_path, file)
        new_file_name = file.replace("Selada_", "Orange_")
        new_file_path = os.path.join(folder_path, new_file_name)
        os.rename(file_path, new_file_path)