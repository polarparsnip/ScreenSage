/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './MediaLists.module.scss'
import { useEffect, useState } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';
import { Loader } from '../../components/Loader/Loader';
import { useUserContext } from '../../context';
import Paging from '../../components/Paging/Paging';
import { ListCard } from '../../components/ListCard/ListCard';
import { Button } from '@mui/material';
import Snackbar from '../../components/Snackbar/Snackbar';

const apiUrl = import.meta.env.VITE_API_URL;

export default function MediaLists({ listType }: { listType: string }) {
  document.title = 'Media lists';

  const navigate = useNavigate();
  const loginContext = useUserContext();
  const { login } = loginContext.userLoggedIn;
  const [searchParams, setSearchParams] = useSearchParams();
  const pageNr = searchParams.get('page') || 1;

  const { id } = useParams();

  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [fail, setFail] = useState<boolean>(false);
  const [failMessage, setFailMessage] = useState<string | null>(null);
  const [success, setSuccess] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [cookies] = useCookies(['token']);
  const [listCreated, setListCreated] = useState<string | null>(null);

  const pathString = listType == 'watchlists' ? 'users/profile/watchlists' : listType == 'userLists' ? 'users/profile/lists': 'lists';

  interface MediaFormData {
    title: string;
    description: string;
    type: 'movies' | 'shows' | '';
    watchlist: boolean;
    sharedWith: [];
  }

  const [formData, setFormData] = useState<MediaFormData>({
    title: '',
    description: '',
    type: '',
    watchlist: listType == 'watchlists',
    sharedWith: []
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value as 'movies' | 'shows' | '' }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await createList(formData);
  };

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const res = await fetch(`${apiUrl}/${pathString}?page=${pageNr}`, {
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
  }, [cookies.token, id, pageNr, pathString, listType, listCreated]);

  const createList = async (data: any): Promise<void> => {
    try {
      const res = await fetch(`${apiUrl}/${listType == 'watchlists' ? 'watchlists' : 'lists'}`, {
        method: 'POST',
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
      // const result = await res.json();
      setListCreated('List created');
      setFormData(
        { title: '', description: '', type: '', watchlist: listType == 'watchlists', sharedWith: [] }
      );

      setSuccessMessage('List created successfully');
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
    <div className={s.media_lists}>
      <div className={s.media_lists__title}>
        <h1>
          {listType == 'watchlists' 
          ? 'My watchlists' 
          : listType == 'userLists' 
            ? 'My media lists' 
            : 'Latest media lists'
          }
        </h1>
      </div>
      <div className={s.media_lists__body}>
        {data && data.content?.map((list: any, i: number) => {
          return (
            <div key={i} className={s.media_lists__card_container}>
              <ListCard list={list} />
            </div>
          );
        })}
      </div>
      {data && data.content && data.start && data.end && <Paging
        page={Number(searchParams.get('page')) || 1}
        setPage={(val: number) => {
          searchParams.set('page', val.toString());
          setSearchParams(searchParams);
        }}
        size={data.size}
        totalPages={data.totalPages}
        totalElements={data.totalElements}
        start={data.start}
        end={data.end}
        type={listType == 'watchlists' ? 'watchlists' : 'media lists'}
        top={0}
      />}
      {(listType == 'watchlists' || listType == 'userLists') &&
      <div className={s.createFormContainer}>
        <h1>Create a new {listType == 'watchlists' ? 'watchlist' : 'media list'}</h1>
        <form className={s.formContainer} onSubmit={handleSubmit}>
          <input
            type={'text'}
            name={'title'}
            placeholder={'Title'}
            value={formData.title}
            onChange={handleChange}
            className={s.inputField}
            required
          />
          <textarea
            name={'description'}
            placeholder={'Description'}
            value={formData.description}
            onChange={handleChange}
            className={s.inputField}
            // required
          />
          <div className={s.formButton}>
            <Button variant='outlined' type='submit'>
              Create Media List
            </Button>
          </div>
        </form>
      </div>
      }
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
    </div>
  )
};