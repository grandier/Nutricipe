import os

folder_path = "C:/Users/fadel/Downloads/labels/carrot/validation"  # Ganti dengan path direktori/folder yang sesuai

# Mendapatkan daftar file dalam folder
files = os.listdir(folder_path)

# Iterasi melalui setiap file
for file in files:
    if file.startswith("Image_") and file.endswith(".txt"):
        file_path = os.path.join(folder_path, file)
        new_file_name = file.replace("Image_", "Carrot_")
        new_file_path = os.path.join(folder_path, new_file_name)
        os.rename(file_path, new_file_path)
