import { useEffect, useState } from 'react';
import s from './Media.module.scss'
import { useCookies } from 'react-cookie';
import { MediaCard } from '../../components/MediaCard/MediaCard';
import { FeaturedMedia } from '../../components/FeaturedMedia/FeaturedMedia';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Paging from '../../components/Paging/Paging';
import { Media as MediaType, MediaList } from '../../types';
import { useUserContext } from '../../context';
import { Loader } from '../../components/Loader/Loader';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';
import MediaFilter from '../../components/MediaFilter/MediaFilter';

const apiUrl = import.meta.env.VITE_API_URL;

export default function Media({ type }: { type: string }) {
  const navigate = useNavigate();
  const loginContext = useUserContext();
  const { login } = loginContext.userLoggedIn;

  const [searchParams, setSearchParams] = useSearchParams();
  const pageNr = searchParams.get('page') || 1;
  const genreId = searchParams.get('genre');
  const search = searchParams.get('search');

  const [data, setData] = useState<MediaList>();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [genres, setGenres] = useState<any>();
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [cookies] = useCookies(['token']);

  document.title = type == 'shows' ? 'Shows' : type == 'movies' ? 'Movies' : 'Anime';

  useEffect(() => {
    setLoading(true);
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/${type}?page=${pageNr}&search=${search || ''}${genreId ? `&genreId=${genreId}` : ''}`, {
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

        if (result && result.results && result.results.length > 0 && result.page) {
          const start = ((result.page - 1) * 20) + 1 ;
          const end = start + 20 - 1;
          result.start = start;
          result.end = end;
        }

        const genreType = type == 'shows' ? 'shows' : type == 'anime' ? 'shows' : 'movies';

        const genRes = await fetch(`${apiUrl}/${genreType}/genres`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${cookies.token}`,
          }
        });

        if (genRes && !genRes.ok) {
          if (genRes.headers.get('content-type')?.includes('application/json')) {
            console.error('Error:', genRes.status, genRes.statusText);
            const message = await genRes.json();
            console.error(message.error);
            throw new Error(message.error || 'Unknown error');
          } else {
            const message = await genRes.text();  // Plain text response
            console.error(message);
            throw new Error(message || 'Unknown error');
          }
        }
        const genresResult = await genRes.json();
        if (genresResult.genres) {
          setGenres(genresResult.genres);
        }

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
  }, [cookies.token, genreId, pageNr, search, type]);

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
    <div className={`${s.mediaPage} ${loading ? 'hidden' : 'fade-in'}`}>
      {data && pageNr == 1 && <FeaturedMedia media={data.results[0]} type={type}></FeaturedMedia>}
      <MediaFilter genres={genres} />
      <div className={s.media}>
        {data && data.results.map((media: MediaType, index: number) => {
          return (
            <div key={index}>
              <MediaCard media={media} type={type} i={index} />
            </div>
          );
        })}
      </div>
      {data && data.start && data.end && <Paging
        page={Number(searchParams.get('page')) || 1}
        setPage={(val: number) => {
          searchParams.set('page', val.toString());
          setSearchParams(searchParams);
        }}
        size={20}
        totalPages={data?.total_pages}
        totalElements={data?.total_results}
        start={data?.start}
        end={data?.end}
        type={type}
        top={0}
      />}
    </div>
  );
};