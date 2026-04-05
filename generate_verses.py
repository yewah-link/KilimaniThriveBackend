import urllib.request
import json
import random
import os

url = 'https://raw.githubusercontent.com/thiagobodruk/bible/master/json/en_web.json'

try:
    print("Downloading Bible JSON...")
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
    response = urllib.request.urlopen(req)
    data = json.loads(response.read().decode('utf-8'))
    
    powerful_books = ["Psalms", "Proverbs", "Isaiah", "Matthew", "Mark", "Luke", "John", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "James", "1 Peter", "1 John", "Hebrews"]
    
    scriptures = []
    
    for book in data:
        if book.get("name") in powerful_books:
            book_name = book.get("name")
            chapters = book.get("chapters", [])
            for ch_idx, verses in enumerate(chapters):
                for v_idx, text in enumerate(verses):
                    # Filter for good length
                    if 50 < len(text) < 180:
                        # Clean quotes
                        clean_text = text.replace('"', "'").strip()
                        scriptures.append({
                            "quote": clean_text,
                            "reference": f"{book_name} {ch_idx+1}:{v_idx+1}"
                        })
                        
    print(f"Found {len(scriptures)} viable verses.")
    
    # Pick 300 random distinct verses
    random.shuffle(scriptures)
    selected = scriptures[:300]
    
    output_path = r"c:\Users\Lincoln Yewah\kilimani-thrive\src\app\features\home\components\daily-scripture\scriptures.data.ts"
    
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    
    with open(output_path, "w", encoding="utf-8") as f:
        f.write("export interface Scripture {\n  quote: string;\n  reference: string;\n}\n\n")
        f.write("export const SCRIPTURES: Scripture[] = [\n")
        for s in selected:
            f.write(f'  {{ quote: "{s["quote"]}", reference: "{s["reference"]}" }},\n')
        f.write("];\n")
        
    print(f"Successfully wrote {len(selected)} scriptures to {output_path}")

except Exception as e:
    print('Error:', e)
