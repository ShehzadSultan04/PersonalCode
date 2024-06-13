# FILL IN ALL THE FUNCTIONS IN THIS TEMPLATE
# MAKE SURE YOU TEST YOUR FUNCTIONS WITH MULTIPLE TEST CASES
# ASIDE FROM THE SAMPLE FILES PROVIDED TO YOU, TEST ON YOUR OWN FILES

# WHEN DONE, SUBMIT THIS FILE TO CANVAS

from collections import defaultdict
from collections import Counter

# YOU MAY NOT CODE ANY OTHER IMPORTS

# ------ TASK 1: READING DATA  --------

# 1.1
def read_ratings_data(f):
    # parameter f: movie ratings file name f (e.g. "movieRatingSample.txt")
    # return: dictionary that maps movie to ratings
    # WRITE YOUR CODE BELOW
        
    ratings_dict = defaultdict(list)
        
    with open(f, 'r') as file: #open file in read mode, file name is from parameter
        for line in file: #iterate through each line in the file
            movie, rating, _ = line.strip().split('|') #split each line by the pipe character, discarding the id of the user at the end
            movie = movie.strip() #strip the movie name of any trailing or leading white spaces
            rating = rating.strip() #strip the rating of any trailing or leading white spaces

            ratings_dict[movie].append(float(rating))#append the rating to the list of ratings for that movie, if not present then create key
        
    return ratings_dict #rerturn the created dictionary

# 1.2
def read_movie_genre(f):
    # parameter f: movies genre file name f (e.g. "genreMovieSample.txt")
    # return: dictionary that maps movie to genre
    # WRITE YOUR CODE BELOW
        
    genre_dict = defaultdict(list)
        
    with open(f, 'r') as file: #open file in read mode, file name is from parameter
        for line in file: #iterate through each line in the file
            genre, _, movie = line.strip().split('|') #split each line by the pipe character
            movie = movie.strip()  #strip the movie names of any trailing or leading white spaces
            genre = genre.strip()  #strip the ratings of any trailing or leading white spaces
            genre_dict[movie] = genre #add a new pair to the dictionary with the movie as the key and the genre as the value
        
    return genre_dict #return the created dictionary

# ------ TASK 2: PROCESSING DATA --------

# 2.1
def create_genre_dict(d):
    # parameter d: dictionary that maps movie to genre
    # return: dictionary that maps genre to movies
    # WRITE YOUR CODE BELOW
    
    genre_dict = defaultdict(list)
    
    for movie, genre in d.items(): #for all movie and genre pairs in the dictionary
        genre_dict[genre].append(movie) #append the movie to the list of movies for that genre, if not present then create the key

    return genre_dict
# 2.2
def calculate_average_rating(d):
    # parameter d: dictionary that maps movie to ratings
    # return: dictionary that maps movie to average rating
    # WRITE YOUR CODE BELOW
    average_dict = defaultdict(list)

    for movie, ratings in d.items():
        average_dict[movie] = sum(ratings) / len(ratings)

    return average_dict
    
# ------ TASK 3: RECOMMENDATION --------

# 3.1
def get_popular_movies(d, n=10):
    # parameter d: dictionary that maps movie to average rating
    # parameter n: integer (for top n), default value 10
    # return: dictionary that maps movie to average rating, 
    #         in ranked order from highest to lowest average rating
    # WRITE YOUR CODE BELOW

    sorted_movies = sorted(d.items(), key=lambda x: x[1], reverse=True) # sort the movies based on average rating in descending order
    popular_movies = dict(sorted_movies[:n]) # get the top n movies from this sorted list
    
    return popular_movies

# 3.2
def filter_movies(d, thres_rating=3):
    # parameter d: dictionary that maps movie to average rating
    # parameter thres_rating: threshold rating, default value 3
    # return: dictionary that maps movie to average rating
    # WRITE YOUR CODE BELOW

    filtered_movies = {movie: rating for movie, rating in d.items() if rating >= thres_rating}
    
    return filtered_movies
    
# 3.3
def get_popular_in_genre(genre, genre_to_movies, movie_to_average_rating, n=5):
    # parameter genre: genre name (e.g. "Comedy")
    # parameter genre_to_movies: dictionary that maps genre to movies
    # parameter movie_to_average_rating: dictionary  that maps movie to average rating
    # parameter n: integer (for top n), default value 5
    # return: dictionary that maps movie to average rating
    # WRITE YOUR CODE BELOW

    movies_in_genre = genre_to_movies.get(genre, []) # get a list of the movies for the given genre from the paramtere
    genre_movies_ratings = {movie: movie_to_average_rating[movie] for movie in movies_in_genre} # create a dictionary of movie to average rating for the movies in the genre
    sorted_movies = sorted(genre_movies_ratings.items(), key=lambda x: x[1], reverse=True) # sort the movies based on average rating in descending order
    popular_movies = dict(sorted_movies[:n]) # get the top n movies from this sorted list
    return popular_movies
    
# 3.4
def get_genre_rating(genre, genre_to_movies, movie_to_average_rating):
    # parameter genre: genre name (e.g. "Comedy")
    # parameter genre_to_movies: dictionary that maps genre to movies
    # parameter movie_to_average_rating: dictionary  that maps movie to average rating
    # return: average rating of movies in genre
    # WRITE YOUR CODE BELOW

    movies_in_genre = genre_to_movies.get(genre, []) # get a list of the movies for the given genre from the paramtere
    genre_movies_ratings = {movie: movie_to_average_rating[movie] for movie in movies_in_genre} # create a dictionary of movie to average rating for the movies in the genre
    
    average = sum(genre_movies_ratings.values()) / len(genre_movies_ratings)
    return average
    
# 3.5
def genre_popularity(genre_to_movies, movie_to_average_rating, n=5):
    # parameter genre_to_movies: dictionary that maps genre to movies
    # parameter movie_to_average_rating: dictionary  that maps movie to average rating
    # parameter n: integer (for top n), default value 5
    # return: dictionary that maps genre to average rating
    # WRITE YOUR CODE BELOW

    genre_ratings = defaultdict(list)

    for genre, _ in genre_to_movies.items():
        genre_ratings[genre] = get_genre_rating(genre, genre_to_movies, movie_to_average_rating)
    sorted_genres = dict(sorted(genre_ratings.items(), key=lambda x: x[1], reverse=True))
    top_genres = dict(list(sorted_genres.items())[:n])
    return top_genres

# ------ TASK 4: USER FOCUSED  --------

# 4.1
def read_user_ratings(f):
    # parameter f: movie ratings file name (e.g. "movieRatingSample.txt")
    # return: dictionary that maps user to list of (movie,rating)
    # WRITE YOUR CODE BELOW
    user_ratings = defaultdict(list)

    with open(f, 'r') as file:
        for line in file:
            movie, rating, ID = line.strip().split('|')
            if ID not in user_ratings:
                user_ratings[ID] = []
            user_ratings[ID].append((movie, rating))

    return user_ratings
    
# 4.2
def get_user_genre(user_id, user_to_movies, movie_to_genre):
    # parameter user_id: user id
    # parameter user_to_movies: dictionary that maps user to movies and ratings
    # parameter movie_to_genre: dictionary that maps movie to genre
    # return: top genre that user likes
    # WRITE YOUR CODE BELOW

    user_ratings = user_to_movies.get(user_id, []) # get the list of movies and ratings for the user
    
    genre_ratings = defaultdict(list)
    
    for movie, rating in user_ratings: # iterate through each movie and rating in the user's list
        genre = movie_to_genre[movie] # get the genre for the movie
        genre_ratings[genre].append(float(rating)) # append the rating to the list of ratings for that genre
    
    genre_averages = {genre: sum(ratings) / len(ratings) for genre, ratings in genre_ratings.items()} # calculate the average rating for each genre
    
    top_genre = max(genre_averages, key=genre_averages.get) # get the genre with the highest average rating
    
    return top_genre
# 4.3    
def recommend_movies(user_id, user_to_movies, movie_to_genre, movie_to_average_rating):
    # get the user's top genre
    top_genre = get_user_genre(user_id, user_to_movies, movie_to_genre)
    
    # get the movies in the user's top genre
    movies_in_genre = create_genre_dict(movie_to_genre)[top_genre]

    movies = [m[0] for m in user_to_movies[user_id]] #get a list of movies that the user has already rated
    unrated_movies = [movie for movie in movies_in_genre if movie not in movies] #get a list of the movies that the user has not rated from the user's top genre using 
    unrated_movies_ratings = {movie: movie_to_average_rating[movie] for movie in unrated_movies}
    sorted_movies = sorted(unrated_movies_ratings.items(), key=lambda x: x[1], reverse=True)
    recommended_movies = dict(sorted_movies[:3])
    
    return recommended_movies
# -------- main function for your testing -----
def main():
    # write all your test code here
    # this function will be ignored by us when grading
    ratingsList = read_ratings_data("myratings.txt")
    print("1.1: " + str(ratingsList))

    print()

    genresList = read_movie_genre("mygenres.txt")
    print("1.2: " + str(genresList))

    print()

    mylist3 = create_genre_dict(genresList)
    print("2.1: " + str(mylist3))

    print()

    mylist4 = calculate_average_rating(ratingsList)
    print("2.2: " + str(mylist4))

    print()

    mylist5 = get_popular_movies(mylist4)
    print("3.1: " + str(mylist5))

    print()

    mylist6 = filter_movies(mylist4)
    print("3.2: " + str(mylist6))

    print()

    mylist7 = get_popular_in_genre("Comedy", mylist3, mylist4, 2)
    print("3.3: " + str(mylist7))

    print()

    mylist8 = get_genre_rating("Comedy", mylist3, mylist4)
    print("3.4: " + str(mylist8))

    print()

    mylist9 = genre_popularity(mylist3, mylist4, 3)
    print("3.5: " + str(mylist9))

    print()
    mylist10 = read_user_ratings("myratings.txt")
    print("4.1: " + str(mylist10))

    print()
    mylist11 = get_user_genre("21", mylist10, genresList)
    print("4.2: " + str(mylist11))

    print()
    mylist12 = recommend_movies("21", mylist10, genresList, mylist4)
    print("4.3: " + str(mylist12))
    
# DO NOT write ANY CODE (including variable names) outside of any of the above functions
# In other words, ALL code your write (including variable names) MUST be inside one of
# the above functions
    
# program will start at the following main() function call
# when you execute hw1.py
main()