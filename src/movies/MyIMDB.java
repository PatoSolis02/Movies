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
 * @author Patricio Solis
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

        movieList.stream()
                .filter(m -> m.getTitleType() == titleType && m.getTitle().contains(words))
                .forEach(result::add);

        return result;
    }

    /**
     * Gets movies by tConst ID in O(1) time.
     *
     * @param ID the movie's tConst string ID
     * @return Movie, movie with tConst ID
     */
   @Override
    public Movie findMovieByID(String ID) {
        // TODO Activity 2.3

        return movieMap.get(ID);
    }

    /**
     * Creates a new TreeSet of movies that are the same type and year and contains genre specified.
     *
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param year the year
     * @param genre the genre, e.g. "Crime", "Drama", etc.
     * @return Collection<Movie> result that contains movies that have the same type, year,
     *          and contain genre specified.
     */
    @Override
    public Collection<Movie> getMoviesByYearAndGenre(String type, int year, String genre) {
        // we use Movie's natural order comparison which is to order Movie's of a
        // type by title and then year
        Set<Movie> result = new TreeSet<>();

        // TODO Activity 3.2
        Genre checkGenre = Genre.valueOf(genre);
        TitleType titleType = TitleType.valueOf(type);

        movieList.stream()
                .filter(m -> m.getGenres().contains(checkGenre) && m.getYear() == year && m.getTitleType() == titleType)
                .forEach(result::add);

        return result;
    }

    /**
     * Creates a new TreeSet of movies that are the same type and are in the range of specified runtime
     * and orders them based on descending runtime and if they are the same runtime then alphabetically
     *
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param start the minimum runtime in minutes (inclusive)
     * @param end the maximum runtime in minutes (inclusive)
     * @return Collection<Movie> result that contains movies that have the same type and are in specified
     *          runtime.
     */
    @Override
    public Collection<Movie> getMoviesByRuntime(String type, int start, int end) {
        // we use a comparator which orders Movie's of a type by descending runtime
        // and then title
        Set<Movie> result = new TreeSet<>(
                (m1, m2) -> { // implemented lambda
                    int num = m2.getRuntimeMinutes() - m1.getRuntimeMinutes();
                    if(num == 0) {
                        num = m1.getTitle().compareTo(m2.getTitle());
                    }
                    return num;
                }
        );
        // TODO Activity 4.2
        TitleType titleType = TitleType.valueOf(type);

        movieList.stream()
                .filter(m -> m.getTitleType() == titleType)
                .filter(m -> m.getRuntimeMinutes() >= start)
                .filter(m -> m.getRuntimeMinutes() <= end)
                .forEach(result::add);

        return result;
    }
    /**
     * Creates a new LinkedList of movies that are the same type and orders them based on descending number
     * of votes.
     *
     * @param num number of movies to list
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @return Collection<Movie> result that contains movies that have the same type and a specified length, num
     *           in descending order.
     */
    @Override
    public Collection<Movie> getMoviesMostVotes(int num, String type) {
        // use a comparator that orders Movie's of a type by descending number
        // of votes
        List<Movie> result = new LinkedList<>();
        // TODO Activity 5.3
        TitleType titleType = TitleType.valueOf(type);

        movieList.stream()
                .filter(m -> m.getTitleType() == titleType)
                .forEach(result::add);

        result.sort(new MovieComparatorVotes());
        return result.subList(0, num);
    }
    /**
     * Creates a new Map with years as keys and the values are lists containing Movie. The lists contain
     * the movies that are the highest rated with a certain type and in a specific year with a minimum
     * of 1000 votes.
     *
     * @param num number of top movies
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param start the start year (inclusive)
     * @param end the end year (inclusive)
     * @return Map<Integer, List<Movie>> top-rated movies in certain years
     */
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