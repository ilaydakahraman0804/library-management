import pandas as pd
import psycopg2

conn = psycopg2.connect(
    host="localhost",
    port="5432",
    database="librarydb",
    user="libraryuser",
    password="librarypass"
)
cursor = conn.cursor()

df = pd.read_csv('Books.csv', sep=';', encoding='latin-1', on_bad_lines='skip', low_memory=False)
df.columns = ['isbn', 'book_title', 'book_author', 'year_of_publication', 'publisher', 'image_url_s', 'image_url_m', 'image_url_l']

df = df[['isbn', 'book_title', 'book_author', 'year_of_publication', 'publisher']]
df = df.dropna(subset=['isbn', 'book_title'])
df['isbn'] = df['isbn'].astype(str).str.strip().str[:20]
df['book_title'] = df['book_title'].astype(str).str.strip()
df['book_author'] = df['book_author'].astype(str).str.strip()
df['publisher'] = df['publisher'].astype(str).str.strip()
df['year_of_publication'] = pd.to_numeric(df['year_of_publication'], errors='coerce')
df = df.drop_duplicates(subset=['isbn'])

count = 0
for _, row in df.iterrows():
    try:
        cursor.execute("""
            INSERT INTO books (isbn, book_title, book_author, year_of_publication, publisher, available)
            VALUES (%s, %s, %s, %s, %s, true)
            ON CONFLICT (isbn) DO NOTHING
        """, (
            row['isbn'],
            row['book_title'],
            row['book_author'],
            None if pd.isna(row['year_of_publication']) else int(row['year_of_publication']),
            row['publisher']
        ))
        count += 1
    except Exception as e:
        continue

conn.commit()
cursor.close()
conn.close()
print(f"{count} kitap başarıyla yüklendi!")
