const fs = require('fs');
const path = require('path');

async function fetchVerses() {
    const url = 'https://raw.githubusercontent.com/thiagobodruk/bible/master/json/en_web.json';

    console.log("Downloading Bible JSON...");
    try {
        const response = await fetch(url);
        const bible = await response.json();
        
        const powerful_books = ["Psalms", "Proverbs", "Isaiah", "Matthew", "Mark", "Luke", "John", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "James", "1 Peter", "1 John", "Hebrews"];
        
        let scriptures = [];
        
        for (const book of bible) {
            if (powerful_books.includes(book.name)) {
                const book_name = book.name;
                const chapters = book.chapters || [];
                
                chapters.forEach((verses, ch_idx) => {
                    verses.forEach((text, v_idx) => {
                        if (text.length > 50 && text.length < 180) {
                            const clean_text = text.replace(/"/g, "'").trim();
                            scriptures.push({
                                quote: clean_text,
                                reference: `${book_name} ${ch_idx + 1}:${v_idx + 1}`
                            });
                        }
                    });
                });
            }
        }
        
        console.log(`Found ${scriptures.length} viable verses.`);
        
        // Shuffle
        for (let i = scriptures.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [scriptures[i], scriptures[j]] = [scriptures[j], scriptures[i]];
        }
        
        const selected = scriptures.slice(0, 300);
        
        const outputPath = path.join('c:', 'Users', 'Lincoln Yewah', 'kilimani-thrive', 'src', 'app', 'features', 'home', 'components', 'daily-scripture', 'scriptures.data.ts');
        
        let fileContent = "export interface Scripture {\\n  quote: string;\\n  reference: string;\\n}\\n\\n";
        fileContent += "export const SCRIPTURES: Scripture[] = [\\n";
        
        for (const s of selected) {
            fileContent += `  { quote: "${s.quote}", reference: "${s.reference}" },\\n`;
        }
        
        fileContent += "];\\n";
        
        fs.writeFileSync(outputPath, fileContent, 'utf-8');
        console.log(`Successfully wrote ${selected.length} scriptures to ${outputPath}`);
        
    } catch (e) {
        console.error("Fetch or Write Error:", e);
    }
}

fetchVerses();
