import unittest
from hw1 import read_ratings_data

class TestReadRatingsData(unittest.TestCase):
    def test_empty_file(self):
        # Test when the file is empty
        ratings_dict = read_ratings_data("empty_file.txt")
        self.assertEqual(ratings_dict, {})

    def test_single_rating(self):
        # Test when there is only one rating in the file
        ratings_dict = read_ratings_data("single_rating.txt")
        expected_dict = {"Movie A": [5.0]}
        self.assertEqual(ratings_dict, expected_dict)

    def test_multiple_ratings(self):
        # Test when there are multiple ratings for different movies in the file
        ratings_dict = read_ratings_data("multiple_ratings.txt")
        expected_dict = {
            "Movie A": [4.0, 3.5, 4.5],
            "Movie B": [2.0, 3.0],
            "Movie C": [5.0]
        }
        self.assertEqual(ratings_dict, expected_dict)

if __name__ == "__main__":
    unittest.main()