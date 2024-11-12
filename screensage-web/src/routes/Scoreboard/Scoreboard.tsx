/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './Scoreboard.module.scss'
import { useEffect, useState } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';
import { Loader } from '../../components/Loader/Loader';
import { useUserContext } from '../../context';
import Paging from '../../components/Paging/Paging';

const apiUrl = import.meta.env.VITE_API_URL;

export default function Scoreboard() {
  document.title = 'Scoreboard';

  const navigate = useNavigate();
  const loginContext = useUserContext();
  const { login, user } = loginContext.userLoggedIn;
  const [searchParams, setSearchParams] = useSearchParams();
  const pageNr = searchParams.get('page') || '1';

  const { id } = useParams();
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [cookies] = useCookies(['token']);


  useEffect(() => {
    setLoading(true);
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/users/scoreboard?page=${pageNr}`, {
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

        if (result && result.content && result.content.length > 0) {
          const start = (result.number * result.size) + 1 ;
          const end = start + result.numberOfElements - 1;
          result.start = start;
          result.end = end;
        }

        setData(result);
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
  }, [cookies.token, id, pageNr]);

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
    <div className={s.scoreboard}>
      <div className={s.scoreboard__title}>
        <div>Daily challenge</div>
        <h1>Scoreboard</h1>
      </div>

      <div className={s.scoreboard__body}>
        <table className={s.scoreboard__table}>
          <thead>
            <tr>
              <th>Rank</th>
              <th>User</th>
              <th>Score</th>
            </tr>
          </thead>
          <tbody>
            {data && data.content?.map((userScore: any, i: number) => {
              const boardRanking = i + ((parseInt(pageNr) - 1) * data?.size) + 1;
              return (
                <tr key={i} className={`${user.id == userScore.user?.id ? s.current : ''}`}>
                  <td className={s.scoreboard__table__rank}>{boardRanking}</td>
                  <td className={s.scoreboard__table__username}>{userScore.user?.username}</td>
                  <td className={s.scoreboard__table__score}>{userScore.totalPoints}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      
      {data && data.content && data.start && data.end && <Paging
        page={Number(searchParams.get('page')) || 1}
        setPage={(val: number) => {
          searchParams.set('page', val.toString());
          setSearchParams(searchParams);
        }}
        size={20}
        totalPages={data.totalPages}
        totalElements={data.totalElements}
        start={data.start}
        end={data.end}
        type={'user scores'}
        top={0}
      />}
    </div>
  )
};