import re
import math

def preprocess_document(document):
    with open(document, 'r') as file:
        document = file.read()
    
    # Remove characters that are not words or whitespaces
    document = re.sub(r"[^\w\s]", "", document)
    
    # Remove extra whitespaces between words
    document = re.sub(r"\s+", " ", document)
    
    # Remove website links
    document = re.sub(r"http\S+|https\S+", "", document)
    
    # Convert all words to lowercase
    document = document.lower()
    
    return document

documents = []

document_dict = {}

# Read the input set of documents from "tfidf_docs.txt"
with open("tfidf_docs.txt", "r") as file:
    documents = file.read().splitlines()
    for document in documents:
        document_dict[document] = preprocess_document(document)
    
with open("stopwords.txt", "r") as file:
    stopwords = file.read().split()

# Remove stopwords from the documents
for document in document_dict:
    words = document_dict[document].split()
    document_dict[document] = " ".join([word for word in words if word not in stopwords])

print(document_dict)
        
# Stem the words in the documents
for file in document_dict:
    document = document_dict[file]
    words = document.split()
    stemmed_document = ""
    for word in words:
        if re.search(r"ing$", word):
            stemmed_word = word[:-3] 
        elif re.search(r"ly$", word):
            stemmed_word = word[:-2]
        elif re.search(r"ment$", word):
            stemmed_word = word[:-4]
        else:
            stemmed_word = word
        stemmed_document += stemmed_word + " "
    document_dict[file]= stemmed_document.strip()

# Now you have the preprocessed documents in the 'documents' list)
preprocess_documents = []

# Write the preprocessed documents to new files, in the format, preproc_<filename>
for document in document_dict:
    output_file = f"preproc_{document}"
    preprocess_documents.append(output_file)
    with open(output_file, "w") as file:
        file.write(document_dict[document])
    pass

# print(preprocess_documents)

# Read the preprocessed set of documents from array preprocessed_files
# Compute Term Frequency (TF) for each document
tf_scores = []
for file in preprocess_documents:
    with open(file, "r") as file:
        document = file.read()
        words = document.split()
        total_terms = len(words)
        word_counts = {}
        for word in words:
            if word in word_counts:
                word_counts[word] += 1
            else:
                word_counts[word] = 1
        tf_scores.append({word: word_counts[word] / total_terms for word in word_counts})

# Compute Inverse Document Frequency (IDF) for each word
idf_scores = {}
for file in preprocess_documents:
    with open(file, "r") as file:
        document = file.read()
        words = set(document.split())
        for word in words:
            if word in idf_scores:
                idf_scores[word] += 1
            else:
                idf_scores[word] = 1

num_documents = len(preprocess_documents)
idf_scores = {word: math.log(num_documents / idf_scores[word]) + 1 for word in idf_scores}

# Compute TF-IDF scores for each word in each document
tfidf_scores = []
for i, file in enumerate(preprocess_documents):
    tfidf_scores.append([])
    for word in tf_scores[i]:
        tfidf_scores[i].append((word, round(tf_scores[i][word] * idf_scores[word], 2)))

i = 0
# Sort and print
#  the top 5 most important words in each document
for document in document_dict:
    tfidf_scores[i].sort(key=lambda x: (-x[1], x[0]))
    output_file = f"tfidf_{document}"
    with open(output_file, "w") as file:
        file.write(str(tfidf_scores[i][:5]))
    i += 1