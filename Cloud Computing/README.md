# Backend for Nutricipe
**Powered by:**

<p style="text-align: center; background-color: #eee; display: inline-block; padding: 14px 20px; border-radius: 15px;">
<img src="https://upload.wikimedia.org/wikipedia/commons/5/51/Google_Cloud_logo.svg" width="250"/>
</p>

### Prerequisites
Before you begin, make sure you have the following software installed on your system:


- Node.js (version 12 or above)

- NPM (Node Package Manager)

### Installation

1. Generate credentials json from firestore and named it to "KeyFile.json".

![Alt Text](https://www.linkpicture.com/q/credentials.png)
2. Put Your KeyFile.json into "/Cloud Computing".

![Alt Text](https://www.linkpicture.com/q/Keyfile.png)

3. Install Dependencies
```sh
npm ci
```

4. Start the Apllication
```sh
npm start
```

## Technology Used

There are three uses of technology in Google Cloud. Among them are Firestore, Cloud Run, Cloud Storage. These three services are used as application service
needs on the cloud side to process all requests and data services.

### Cloud Run
<a href='https://www.linkpicture.com/view.php?img=LPic648b411b4f3531818415528'><img src='https://www.linkpicture.com/q/LPic648b411b4f3531818415528.png' type='image' width="50" height="50"></a>
This Cloud Run service **`has been deployed`** on devevelopment environment.

Service details:
```Cloud Run
Container Port  : 8080
Memory          : 1.0 GiB
CPU             : 2 
```

