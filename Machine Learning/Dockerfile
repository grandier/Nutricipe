# Use a base image
FROM python:3.9

# Set the working directory inside the container
WORKDIR /app

# Copy the requirements.txt file to the container
COPY requirements.txt .

# Install the Python dependencies
RUN pip install --no-cache-dir -r requirements.txt
RUN apt-get update && apt-get install -y libgl1-mesa-glx

# Copy the application files to the container
COPY app.py .
COPY best2.pt .
COPY best.pt .

# Expose a port
EXPOSE 5000

# Run the Flask application
CMD ["python", "app.py"]