# 🧠 Image Analyzer API

A RESTful Spring Boot service that extracts and analyzes text from uploaded images or PDF documents using OCR and a local LLM (Large Language Model).

## 📦 Features

- ✅ Extract text from PDFs and images (`.pdf`, `.jpg`, `.png`, etc.)
- 🤖 Analyze content using a local LLM
- 📜 Generate intelligent image descriptions
- 📂 Support for multipart/form-data file uploads
- 🛡️ Global error handling with standardized JSON responses
- 📘 Swagger UI auto-generated documentation

---

## 🚀 Getting Started

### 🔧 Requirements

- Java 21+
- Maven 3.9+
- Tesseract OCR installed locally and accessible in `PATH`
- Ollama

### 🛠️ Setup

Clone the repository:

```bash
git clone https://github.com/alexandradew/image-analyzer.git
cd image-analyzer
```

Adjust the properties based on the example file:

```bash
spring.application.name=pdf-analyzer

tessdata.path=C:/tessdata
tessdata.lang=por

llmdata.textmodel=gemma:2b
llmdata.imagemodel=llava
llmdata.url=http://localhost:11434/api/generate
```
1. You'll have to have tesseract installed in your OS
2. You'll have to download the trained data for you target language data extraction in: https://github.com/tesseract-ocr/tessdata
3. If you're gonna use gemma and llava as the models as in the example, you'll have to pull it using ollama.

```bash
ollama pull gemma:2b
```

```bash
ollama pull llava
```

Assuming everything is installed, you'll need to make sure ollama is running:

```bash
ollama serve
```

### Running the application

1. Build the project:
```bash
   ./mvnw clean install   # (Linux/macOS)
   mvnw.cmd clean install # (Windows) 
 ```

2. Run the application:
```bash
   ./mvnw spring-boot:run   # (Linux/macOS)
   mvnw.cmd spring-boot:run # (Windows)
```

3. Open your browser and go to:
   http://localhost:8080/swagger-ui.html

The API will be available for use and testing from the Swagger UI or via tools like Postman or curl.

### 📤 API USAGE

#### POST /document/extract
Purpose: Extracts text from a PDF or image and analyzes it using a prompt.

Request parameters:
- file: PDF or image file (multipart/form-data)
- prompt: Text prompt to guide the analysis

Example using cURL:
```bash
curl -X POST http://localhost:8080/document/extract \
-F "file=@/path/to/your/file.pdf" \
-F "prompt=Summarize the content"
```

#### POST /image/analyze
Purpose: Analyzes an image file directly and returns a description based on a prompt.

Request parameters:
- file: Image file (multipart/form-data)
- prompt: Description request (e.g., "Describe what is happening")

Example using cURL:
```bash
curl -X POST http://localhost:8080/image/analyze \
-F "file=@/path/to/your/image.jpg" \
-F "prompt=What is happening in this image?"
```

✅ Example Success Response:
```json
{
    "status": 200,
    "message": "Image analyzed successfully",
    "content": "The image shows a man standing near a mountain..."
}
```

❌ Example Error Response:
```json
{
    "status": 400,
    "error": "Missing required field",
    "field": "prompt",
    "content": "Parameter is required"
}
```

