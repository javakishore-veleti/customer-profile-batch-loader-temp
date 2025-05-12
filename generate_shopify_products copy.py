import os
import json
import uuid
from datetime import datetime
from random import randint, uniform
from faker import Faker

# Initialize Faker for realistic data
faker = Faker()

# Configuration
FILES_TO_GENERATE = 5
PRODUCTS_PER_FILE = 10
OUTPUT_DIR = "shopify-dataset/shopify-products"

# Ensure output directory exists
os.makedirs(OUTPUT_DIR, exist_ok=True)

# Function to generate a single unique product
def generate_product():
    unique_name = f"{faker.color_name()} {faker.word()} {uuid.uuid4().hex[:6]}"
    return {
        "title": unique_name,
        "description": faker.paragraph(),
        "vendor": "KVSoftSol",
        "productType": faker.word().capitalize(),
        "price": round(uniform(10.0, 100.0), 2),
        "costPerItem": round(uniform(5.0, 50.0), 2),
        "sku": uuid.uuid4().hex[:12].upper(),
        "barcode": faker.ean13(),
        "inventoryQuantity": randint(1, 100),
        "weight": round(uniform(0.1, 5.0), 2),
        "tags": [faker.word(), faker.word(), faker.word()]
    }

# Generate and write product files
for i in range(FILES_TO_GENERATE):
    products = [generate_product() for _ in range(PRODUCTS_PER_FILE)]
    timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
    filename = f"products_{timestamp}_{i}.json"
    filepath = os.path.join(OUTPUT_DIR, filename)

    with open(filepath, "w") as f:
        json.dump(products, f, indent=2)

    print(f"Generated: {filepath}")
