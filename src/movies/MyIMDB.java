package movies;

import cs.Genre;
import cs.MovieMaps;
import cs.TitleType;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * The subclass of the IMDB abstract class that implements all the required
 * abstract query methods.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class MyIMDB extends IMDB {
    /**
     * The minimum number of votes a movie needs to be considered for top ranking
     */
    private final static int MIN_NUM_VOTES_FOR_TOP_RANKED = 1000;

    /**
     * Create IMDB using the small or large dataset.
     *
     * @param small true if the small dataset is desired, otherwise the large one
     * @throws FileNotFoundException
     */
    public MyIMDB(boolean small) throws FileNotFoundException {
        super(small);
    }

    /**
     * Creates a new list of movies that contain the substring passed in as words.
     *
     * @param type  the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param words the words as a string that the movie title must contain to match
     * @return Collection<Movie> result that contains movies that contain specific substring
     */
    @Override
    public Collection<Movie> getMovieTitleWithWords(String type, String words) {
        // we simply loop over movieList and add to our list the movies that
        // have the same type, and contain the words substring
        List<Movie> result = new LinkedList<>();

        // TODO Activity 1.2
        TitleType titleType = TitleType.valueOf("MOVIE");
        for (Movie movie : movieList) {
            if (movie.getTitleType() == titleType && movie.getTitle().contains(words)) {
                result.add(movie);
            }
        }
        return result;
    }

    @Override
    public Movie findMovieByID(String ID) {
        // TODO Activity 2.3

        return movieMap.get(ID);
    }

    @Override
    public Collection<Movie> getMoviesByYearAndGenre(String type, int year, String genre) {
        // we use Movie's natural order comparison which is to order Movie's of a
        // type by title and then year
        Set<Movie> result = new TreeSet<>();

        // TODO Activity 3.2
        Genre checkGenre = Genre.valueOf(genre);
        TitleType titleType = TitleType.valueOf(type);
        for (Movie movie : movieList) {
            if (movie.getGenres().contains(checkGenre) && movie.getYear() == year && movie.getTitleType() == titleType) {
                result.add(movie);
            }
        }
        return result;
    }

    @Override
    public Collection<Movie> getMoviesByRuntime(String type, int start, int end) {
        // we use a comparator which orders Movie's of a type by descending runtime
        // and then title
        Set<Movie> result = new TreeSet<>(new MovieComparatorRuntime());

        // TODO Activity 4.2
        TitleType titleType = TitleType.valueOf(type);
        for (Movie movie : movieList) {
            if (movie.getTitleType() == titleType && movie.getRuntimeMinutes() >= start
                    && movie.getRuntimeMinutes() <= end) {
                result.add(movie);
            }
        }
        return result;
    }

    @Override
    public Collection<Movie> getMoviesMostVotes(int num, String type) {
        // use a comparator that orders Movie's of a type by descending number
        // of votes
        List<Movie> result = new LinkedList<>();
        // TODO Activity 5.3
        TitleType titleType = TitleType.valueOf(type);

        for (Movie movie : movieList) {
            if (movie.getTitleType() == titleType) {
                result.add(movie);
            }
        }
        result.sort(new MovieComparatorVotes());
        return result.subList(0, num);
    }

    @Override
    public Map<Integer, List<Movie>> getMoviesTopRated(int num, String type, int start, int end) {
        Map<Integer, List<Movie>> result = new TreeMap<>();

        // TODO Activity 6.2
        Set<Rating> orderedRatings = new TreeSet<>();
        TitleType titleType = TitleType.valueOf(type);
        for (Movie movie : movieList) {
            if (movie.getRating().getNumVotes() >= MIN_NUM_VOTES_FOR_TOP_RANKED && movie.getTitleType() == titleType &&
                    movie.getYear() >= start && movie.getYear() <= end) {
                orderedRatings.add(movie.getRating());
            }
        }
        for (int i = start; i <= end; i++) {
            result.put(i ,new LinkedList<>());
        }
        for(Rating orderedRating : orderedRatings){
            Movie movie = this.movieMap.get(orderedRating.getID());
            if(result.get(movie.getYear()).size() == 0){
                result.get(movie.getYear()).add(movie);
            }
            else if (result.get(movie.getYear()).size() < num) {
                result.get(movie.getYear()).add(movie);
            }
        }
        return result;
    }
}