/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './Home.module.scss';
import { useUserContext } from '../../context';
import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Loader } from '../../components/Loader/Loader';
import { useCookies } from 'react-cookie';
import { Quote } from '../../components/Quote/Quote';

const apiUrl = import.meta.env.VITE_API_URL;

export default function Home() {
  document.title = 'Home';

  const [cookies] = useCookies(['token']);
  const loginContext = useUserContext();
  const { login } = loginContext.userLoggedIn;

  const [quoteData, setQuoteData] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/quote`, {
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
        setQuoteData(result);
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
  }, [cookies.token]);
  
  if (loading) {
    return (
      <Loader isLoading={loading} />
    );
  }

  return (
    <div className={s.home}>
      {login 
      ?
        <div className={s.home__main}>
          <img src='/img/logo.png' alt='' />
          <div className={s.home__main__buttons}>
            <Link to={'/challenge'}>Daily challenge</Link>
            <Link to={'/media'}>Media</Link>
          </div>
          {error && <h1>{error}</h1>}
          {quoteData && <Quote 
            text={quoteData.text}
            title={quoteData.title}
            year={quoteData.year}
            mediaId={quoteData.mediaId}
            type={quoteData.type}
          />}
        </div>
      :  
        <div className={s.home__main}>
          <img src='/img/logo.png' alt='' />
          <div className={s.home__main__buttons}>
            <Link to={'/about'}>About us</Link>
            <Link to={'/login'}>Log in</Link>
          </div>
        </div>
      }
    </div> 
  )
};