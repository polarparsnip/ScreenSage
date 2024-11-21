/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from 'react';
import s from './MediaList.module.scss'
import { useNavigate, useParams } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';
import { Loader } from '../../components/Loader/Loader';
import { useUserContext } from '../../context';
import { MediaListItem } from '../../components/MediaListItem/MediaListItem';
import Snackbar from '../../components/Snackbar/Snackbar';
import Confirm from '../../components/Confirm/Confirm';
import LikeButton from '../../components/LikeButton/LikeButton';
import SharingSearch from '../../components/SharingSearch/SharingSearch';

const apiUrl = import.meta.env.VITE_API_URL;

export default function MediaList({ listType }: { listType: string }) {
  const navigate = useNavigate();
  const loginContext = useUserContext();
  const { login, user } = loginContext.userLoggedIn;

  const { id } = useParams();
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [fail, setFail] = useState<boolean>(false);
  const [failMessage, setFailMessage] = useState<string | null>(null);
  const [success, setSuccess] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [activeItemID, setActiveItemID] = useState<number | null>(null);

  const [confirmDeleteOpen, setConfirmDeleteOpen] = useState(false);

  const [cookies] = useCookies(['token']);

  const [userHasLiked, setUserHasLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);

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
        setUserHasLiked(result.userHasLiked);
        setLikeCount(result.likeCount);

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
      setSuccessMessage('List has been updated');
      setSuccess(true);

    } catch(error: unknown) {
      if (error instanceof Error) {
        console.error('Error:', error.message)
        setFailMessage(error.message);
        setFail(true);
      } else {
        setFailMessage('An unknown error occurred');
        setFail(true);
      }
    }
  }

  const handleRemove = async (id: number | null) => {
    if (!id) {
      return;
    }

    const updatedItems = data?.mediaListItems?.filter((item: any) => item.id !== id);
    const updatedData = { ...data, mediaListItems: updatedItems };
    
    await updateList(updatedData, true); 
    setActiveItemID(null);
  };

  const likeList = async (): Promise<void> => {
    try {
      const res = await fetch(`${apiUrl}/${listType}/${id}/likes`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${cookies.token}`,
        },
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

      setUserHasLiked((prev) => !prev);
      setLikeCount((prev) => (userHasLiked ? prev - 1 : prev + 1));

      setSuccessMessage(`You ${userHasLiked ? 'removed your like for' : 'liked'} ${data?.title}`);
      setSuccess(true);
      

    } catch(error: unknown) {
      if (error instanceof Error) {
        console.error('Error:', error.message)
        setFailMessage(error.message);
        setFail(true);
      } else {
        setFailMessage('An unknown error occurred');
        setFail(true);
      }
    }
  }

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
      <div className={s.media_list__title_like_container}>
        <div className={s.media_list__title}>
          <h1>{data && data.title ? data.title : 'Media list'}</h1>
          {listType === 'lists' && data && data.description && <div>{data.description}</div>}
        </div>
        {data && data.likeCount != null && data.userHasLiked != null && listType === 'lists' && 
          <LikeButton
            likeCount={likeCount} 
            userHasLiked={userHasLiked} 
            toggleLike={likeList} 
          />
        }
      </div>
      {listType === 'watchlists' && id &&
        <div className={s.share_search}>
          <p>Share list with other users</p>
          <SharingSearch listId={id} currentIds={data.sharedWith}/>
        </div>
      }
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
                  type={item.type}
                  author={data?.user.id == user.id}
                  onRemove={(id: number) => {
                    setActiveItemID(id);
                    setConfirmDeleteOpen(true);
                  }}
                  i={i} 
                />
              </div>
            );
          })}
        </div>
      </div>
      <Snackbar
        type={'success'}
        open={success}
        setOpen={setSuccess}
        setMessage={setSuccessMessage}
      >
        {successMessage}
      </Snackbar>
      <Snackbar
        type={'error'}
        open={fail}
        setOpen={setFail}
        setMessage={setFailMessage}
      >
        {failMessage}
      </Snackbar>
      <Confirm 
        open={confirmDeleteOpen} 
        setOpen={setConfirmDeleteOpen} 
        title={'Delete media item'}
        description={'Are you sure you want to delete this item? This action cannot be undone.'}
        onConfirm={async() => {
          await handleRemove(activeItemID);
        }}
      />
    </div>
  )
};