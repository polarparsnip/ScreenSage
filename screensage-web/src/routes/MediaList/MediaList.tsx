/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from 'react';
import s from './MediaList.module.scss'
import { useNavigate, useParams } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';
import { Loader } from '../../components/Loader/Loader';
import { useUserContext } from '../../context';
import { MediaListItem } from '../../components/MediaListItem/MediaListItem';

const apiUrl = import.meta.env.VITE_API_URL;

export default function MediaList({ listType }: { listType: string }) {
  const navigate = useNavigate();
  const loginContext = useUserContext();
  const { login, user } = loginContext.userLoggedIn;

  const { id } = useParams();
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [fail, setFail] = useState<string | null>(null);
  const [cookies] = useCookies(['token']);

  useEffect(() => {
    setLoading(true);
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/${listType}/${id}`, {
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
  }, [cookies.token, id, listType]);

  const updateList = async (data: any, replace: boolean): Promise<void> => {
    try {
      const res = await fetch(`${apiUrl}/watchlists/${data.id}?replace=${replace}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${cookies.token}`,
        },
        body: JSON.stringify(data)
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
      setFail(null);

    } catch(error: unknown) {
      if (error instanceof Error) {
        console.error('Error:', error.message)
        setFail(error.message);
      } else {
        setFail('An unknown error occurred');
      }
    }
  }

  const handleRemove = async (id: number) => {
    const updatedItems = data?.mediaListItems?.filter((item: any) => item.id !== id);
    const updatedData = { ...data, mediaListItems: updatedItems };
    
    await updateList(updatedData, true); 
  };

  document.title = data?.title ? data.title : 'Media list';

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
    <div className={s.media_list}>
      {fail && 
        <div className={'fail_message'}>
          <h1>{fail}</h1>
        </div>
      }
      <div className={s.media_list__title}>
        <h1>{data && data.title ? data.title : 'Media list'}</h1>
        {listType === 'list' && data && data.description && <div>{data.description}</div>}
      </div>
      <div className={s.media_list__body}>
        {data?.mediaListItems?.length < 1 &&
          <div className={s.noMedia}><h1>No media in list</h1></div>
        }
        <div className={`${data?.mediaListItems?.length < 4 ? s.media_list__few_items : s.media_list__items}`}>
          {data && data.mediaListItems?.map((item: any, i: number) => {
            return (
              <div key={i}>
                <MediaListItem 
                  mediaListItem={item} 
                  type={data.type}
                  author={data?.user.id == user.id}
                  onRemove={handleRemove}
                  i={i} 
                />
              </div>
            );
          })}
        </div>
      </div>
    </div>
  )
};