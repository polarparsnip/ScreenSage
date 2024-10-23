
import s from './MediaInfo.module.scss'
import { useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import { Link, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { Rating, Typography, Modal, Button, ButtonGroup } from '@mui/material';
import { Publish, Theaters, Language, ArrowBack } from '@mui/icons-material';
import { MediaCard } from '../../components/MediaCard/MediaCard';
import { ReviewCard } from '../../components/ReviewCard/ReviewCard';
import Paging from '../../components/Paging/Paging';
import { Genre, Media, MediaDetailed, Page, Review } from '../../types';
import { useUserContext } from '../../context';
import { Loader } from '../../components/Loader/Loader';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';

const apiUrl = import.meta.env.VITE_API_URL;

export default function MediaInfo({ type }: { type: string }) {
  const navigate = useNavigate();
  const loginContext = useUserContext();
  const { login } = loginContext.userLoggedIn;

  const { id } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();

  const pageNr = searchParams.get('page') || 1;

  const [data, setData] = useState<MediaDetailed | null>(null);
  const [recommendations, setRecommendations] = useState<Media[] | null>(null); 
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [cookies] = useCookies(['token']);

  const [reviews, setReviews] = useState<Page | null>(null);
  const [reviewPosted, setReviewPosted] = useState(false);

  const [rating, setRating] = useState<number | null>(null);
  const [reviewRating, setReviewRating] = useState<number | null>(null);
  const [userHasRated, setUserHasRated] = useState<boolean>(false);

  const [open, setOpen] = useState(false);

  document.title = data ? (type == 'shows' || type == 'anime') ? data?.name : data?.title : 'Media info';

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/${type}/${id}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${cookies.token}`,
          }
        });

        if (res && !res.ok) {
          if (res.headers.get('content-type')?.includes('application/json')) {
            console.error('Error:', res.status, res.statusText);
            const message = await res.json();
            console.error(message.error);
            throw new Error(message.error || 'Unknown error');
          } else {
            const message = await res.text();  // Plain text response
            console.error(message);
            throw new Error(message || 'Unknown error');
          }
        }

        const result = await res.json();
        setData(result);

        // if (result?.recent_reviews && result.recent_reviews.length > 0) {
        //   setReviews(result.recent_reviews);
        // }

        const recRes = await fetch(`${apiUrl}/${type}/${id}/recommendations`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${cookies.token}`,
          }
        });
        
        if (recRes && !recRes.ok) {
          if (recRes.headers.get('content-type')?.includes('application/json')) {
            console.error('Error:', recRes.status, recRes.statusText);
            const message = await recRes.json();
            console.error(message.error);
            throw new Error(message.error || 'Unknown error');
          } else {
            const message = await recRes.text();  // Plain text response
            console.error(message);
            throw new Error(message || 'Unknown error');
          }
        }

        const recommendations = await recRes.json();
        if (recommendations.results) {
          setRecommendations(recommendations.results);
        }

        const mediaRating = result?.user_rating 
          ? result?.user_rating 
          : result?.average_rating 
            ? result.average_rating 
            : null
        
        if (result.user_rating) {
          setUserHasRated(true);
        }
        setRating(mediaRating);
        setLoading(false);

      } catch (error: unknown) {
        if (error instanceof Error) {
          setError(error.message);
          setLoading(false);
        } else {
          setError('An unknown error occurred');
          setLoading(false);
        }
      }
    };

    fetchData();
  }, [cookies.token, type, id, reviewPosted, pageNr]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/${type}/${id}/reviews?page=${pageNr}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${cookies.token}`,
          }
        });

        if (res && !res.ok) {
          if (res.headers.get('content-type')?.includes('application/json')) {
            console.error('Error:', res.status, res.statusText);
            const message = await res.json();
            console.error(message.error);
            throw new Error(message.error || 'Unknown error');
          } else {
            const message = await res.text();  // Plain text response
            console.error(message);
            throw new Error(message || 'Unknown error');
          }
        }

        const reviews = await res.json();
        
        if (reviews && reviews.content && reviews.content.length > 0) {
          const start = (reviews.number * reviews.size) + 1 ;
          const end = start + reviews.numberOfElements - 1;
          reviews.start = start;
          reviews.end = end;

          setReviews(reviews);
        }

        // setLoading(false);
      } catch (error: unknown) {
        if (error instanceof Error) {
          setError(error.message);
          // setLoading(false);
        } else {
          setError('An unknown error occurred');
          // setLoading(false);
        }
      }
    };

    fetchData();
  }, [cookies.token, type, id, reviewPosted, pageNr]);

  const postReview = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
    event.preventDefault();
    const target = event.target as HTMLFormElement;
  
    const reviewData = { 
      'rating': target.reviewRating.value, 
      'content': target.reviewContent.value
    }
  
    try {
      const res = await fetch(`${apiUrl}/${type}/${id}/reviews`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${cookies.token}`,
        },
        body: JSON.stringify(reviewData),
      });

      if (res && !res.ok) {
        if (res.headers.get('content-type')?.includes('application/json')) {
          console.error('Error:', res.status, res.statusText);
          const message = await res.json();
          console.error(message.error);
          throw new Error(message.error || 'Unknown error');
        } else {
          const message = await res.text();  // Plain text response
          console.error(message);
          throw new Error(message || 'Unknown error');
        }
      }

      // const result = await response.json();
      setReviewPosted(true);
      setUserHasRated(true);
      // return result;

    } catch(error: unknown) {
      if (error instanceof Error) {
        console.error('Error:', error.message)
        throw new Error(error.message);
      } else {
        throw new Error('An unknown error occurred');
      }
    }
  }

  if (!login) {
    navigate('/', { replace: true });
  }
  
  if (loading) {
    return (
      <Loader isLoading={loading} />
    );
  }

  if (error) {
    return (
      <ErrorDisplay errorMessage={error} />
    );
  }

  return (
    <div className={`${s.media_info} ${loading ? 'hidden' : 'fade-in-slow'}`}>
      <div className={s.container_space_around}>
        <div className={s.poster_div}>
          <img
            src={`https://image.tmdb.org/t/p/w500/${data?.poster_path}`}
            className={s.poster}
            alt={(type == 'shows' || type == 'anime') ? data?.name : data?.title}
          />
        </div>
        <div className={s.info_container}>
          <div className={s.mediaTitle}>
            <Typography variant='h3' align='center' gutterBottom>
              {(type == 'shows' || type == 'anime') ? data?.name : data?.title} ({data?.release_date 
                ? data.release_date.split('-')[0] 
                : data?.first_air_date 
                  ? data.first_air_date.split('-')[0] 
                  : null
              })
            </Typography>
            <Typography variant='h5' align='center' gutterBottom>
              {data?.tagline}
            </Typography>
          </div>
          <div className={s.rating_runtime_container}>
            <div className={s.rating_container}>
              <div className={s.rating}>
                {rating == null && <Typography component='legend'>No rating given</Typography>}
                <Rating 
                  value={rating}
                  precision={0.5}
                  readOnly
                  sx={{
                    // '& .MuiRating-iconFilled': {
                    //   color: userHasRated ? 'var(--color-rating-reviewed)': 'var(--color-rating-normal)',
                    // },
                    '& .MuiRating-iconEmpty': {
                      color: 'lightgray',
                    },
                  }}
                />
              </div>
              <Typography gutterBottom variant='subtitle1' style={{ marginLeft: '10px' }}>
                {rating && `${rating}/5`}
              </Typography>
            </div>
            {data?.runtime ? <div>{data.runtime}min</div> : data?.number_of_episodes ? <div>{data?.number_of_episodes} episodes</div> : <></>}
          </div>
          <div className={s.genres_container}>
            {data?.genres?.map((genre: Genre) => (
              <div className={s.links} key={genre.name}>
                <div>{genre?.name}</div>
              </div>
            ))}
          </div>
          <div className={s.overview_container}>
            <Typography variant='h5' gutterBottom style={{ marginTop: '20px' }}>Overview</Typography>
            <Typography style={{ marginBottom: '2rem' }}>{data?.overview}</Typography>
          </div>
          <div className={s.buttons_container}>
            <div className={s.button_container}>
              <div className={s.button_container}>
                <ButtonGroup size='small' variant='outlined'>
                  <Button
                    component='a' 
                    target='_blank' 
                    rel='noopener noreferrer' 
                    href={data?.homepage} 
                    endIcon={<Language />}
                  >
                    Website
                  </Button>
                  {data?.videos?.results?.length && data?.videos?.results?.length > 0 && <Button onClick={() => setOpen(true)} href='#' endIcon={<Theaters />}>Trailer</Button>}
                </ButtonGroup>
              </div>
              <div className={s.buttonContainer}>
                <ButtonGroup size='small' variant='outlined'>
                  <Button endIcon={<ArrowBack />} >
                    <Typography variant='subtitle2' component={Link} to={`/${type}`} color='inherit' sx={{ textDecoration: 'none' }}>
                      Back
                    </Typography>
                  </Button>
                </ButtonGroup>
              </div>
            </div>
          </div>
        </div>

      </div>
      <div className={s.review_container}>
        <h3 style={{textAlign: 'center', marginBottom: '1rem'}}>
          Reviews
        </h3>
        {reviews && reviews.content ? (
          <div className={s.reviews}>
            {reviews.content.map((review: Review, index: number) => (
              <div key={index}>
                <ReviewCard review={review} />
              </div>
            ))}
          </div>
          ) : (
            <div className={s.no_reviews}>No reviews yet.</div>
        )}

        {reviews && reviews.start && reviews.end && <Paging 
          page={Number(searchParams.get('page')) || 1}
          setPage={(val: number) => {
            searchParams.set('page', val.toString());
            setSearchParams(searchParams);
          }}
          size={reviews?.size}
          totalPages={reviews?.totalPages}
          totalElements={reviews?.totalElements}
          start={reviews?.start}
          end={reviews?.end}
          type={'reviews'}
          top={searchParams.get('page') ? 680 : 0}
        />}
        
      </div>
        {!userHasRated ? (
              <div className={s.createReview}>
                <h3 style={{textAlign: 'center', marginBottom: '1rem'}}>Post a review</h3>
                <form className={s.create_review__form}
                  onSubmit={async (event) => {
                    event.preventDefault();
                    try {
                      await postReview(event);
                    } catch(error: unknown) {
                      if (error instanceof Error) {
                        setError(error.message);
                      } else {
                        setError('An unknown error occurred');
                      }
                    }
                  }}
                >
                  {error && <h3>{error}</h3>}

                  <Rating
                    name='reviewRating'
                    value={reviewRating}
                    precision={0.5}
                    onChange={(_event, newValue) => {
                      setReviewRating(newValue);
                    }}
                    sx={{
                      '& .MuiRating-iconEmpty': {
                        color: 'lightgray',
                      },
                    }}
                  />

                  <textarea id='reviewContent' placeholder='Write a review' required/>

                  {/* <ButtonGroup size='small' variant='outlined'> */}
                  <Button endIcon={<Publish />} type='submit' variant='outlined' sx={{ borderColor: 'primary.main' }}>Post review</Button>
                  {/* </ButtonGroup> */}
                </form>
              </div>
        ) : null }
        <div className={s.recommendations}>
          <Typography variant='h3' gutterBottom align='center'>
            You might also like
          </Typography>
          {/* {recommendations
            ? <MovieList movies={recommendations} numberOfMovies={12} />
            : <div>Sorry, nothing was found.</div>
          } */}
          {recommendations && recommendations.length > 0 ? (
            <div className={s.recommendations__media}>
              {recommendations.slice(0, 4).map((media: Media, index: number) => (
                <div key={index}>
                  <MediaCard media={media} type={type} i={index} />
                </div>
              ))}
            </div>
          ) : (
            <div>Sorry, nothing was found.</div>
          )}
        
        </div>


        <Modal
          closeAfterTransition
          className={s.modal}
          open={open}
          onClose={() => setOpen(false)}
        >
          {data?.videos?.results?.length && data?.videos?.results?.length > 0 ? (
            <iframe
              className={s.video}
              style={{ border: 'none' }}
              title={'Trailer'}
              src={`https://www.youtube.com/embed/${data.videos.results[0].key}?autoplay=1`}
              allow='autoplay'
            />
          ) : <></>}
        </Modal>
    </div>
  );
};