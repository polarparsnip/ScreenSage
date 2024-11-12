import { useEffect, useState } from 'react';
import s from './Profile.module.scss'
import { useCookies } from 'react-cookie';
import { ReviewCard } from '../../components/ReviewCard/ReviewCard';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Paging from '../../components/Paging/Paging';
import { Button } from '@mui/material';
import { Page, Review, User } from '../../types';
import { useUserContext } from '../../context';
import { Loader } from '../../components/Loader/Loader';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';
import Snackbar from '../../components/Snackbar/Snackbar';

const apiUrl = import.meta.env.VITE_API_URL;

export default function Profile() {
  document.title = 'Profile';

  const navigate = useNavigate();
  const loginContext = useUserContext();
  const { login } = loginContext.userLoggedIn;

  const [searchParams, setSearchParams] = useSearchParams();
  const pageNr = searchParams.get('page') || 1;

  const [cookies, setCookie] = useCookies(['token']);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [fail, setFail] = useState<boolean>(false);
  const [failMessage, setFailMessage] = useState<string | null>(null);
  const [success, setSuccess] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [user, setUser] = useState<User | null>(null);
  const [userReviews, setUserReviews] = useState<Page | null>(null);

  const [profilePicture, setProfilePicture] = useState<string | null>(null);
  const [username, setUsername] = useState<string>('CurrentUsername');
  const [newUsername, setNewUsername] = useState<string | null>(null);
  const [password, setPassword] = useState<string>('********');
  const [newPassword, setNewPassword] = useState<string | null>(null);
  const [editMode, setEditMode] = useState<{ username: boolean; password: boolean }>({
    username: false,
    password: false,
  });


  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/users/profile`, {
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
            setError(message.error);
          } else {
            const message = await res.text();  // Plain text response
            console.error(message);
            setError(message);
          }
        }

        const userInfo = await res.json();

        if (userInfo.user) {
          setUser(userInfo.user);
          setUsername(userInfo.user.username);
          setPassword(userInfo.user.passwordPlaceholder);
          setProfilePicture(userInfo.user.profileImg);
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
  }, [cookies.token, editMode]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/users/profile/reviews?page=${pageNr}`, {
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

        const userReviews = await res.json();
        
        if (userReviews && userReviews.content && userReviews.content.length > 0) {
          const start = (userReviews.number * userReviews.size) + 1 ;
          const end = start + userReviews.numberOfElements - 1;
          userReviews.start = start;
          userReviews.end = end;
          setUserReviews(userReviews);
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
  }, [cookies.token, pageNr]);

  const updateUsername = async (): Promise<void> => {
    let res;
    try {
      res = await fetch(`${apiUrl}/users/profile/username`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${cookies.token}`,
        },
        body: JSON.stringify({ 'username': newUsername }),
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
          throw new Error(message|| 'Unknown error');
        }
      }

      const result = await res.json();
 
      if (result.user && result.token) {
        setCookie('token', result.token, {
          path: '/',
          maxAge: 24 * 60 * 60,
          secure: true,
        });
      }

      setNewUsername(null);
      setSuccessMessage('Username updated');
      setSuccess(true);

    } catch(error: unknown) {
      if (error instanceof Error) {
        setFailMessage(error.message);
        setFail(true);
      } else {
        setFailMessage('An unknown error occurred');
        setFail(true);
      }
    }
  }

  const updatePassword = async (): Promise<void> => {
    let res;
    try {
      res = await fetch(`${apiUrl}/users/profile/password`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${cookies.token}`,
        },
        body: JSON.stringify({ 'password': newPassword }),
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
      
      setNewPassword(null);
      setSuccessMessage('Password updated');
      setSuccess(true);

    } catch(error: unknown) {
      if (error instanceof Error) {
        setFailMessage(error.message);
        setFail(true);
      } else {
        setFailMessage('An unknown error occurred');
        setFail(true);
      }
    }
  }

  const updateProfileImage = async (newProfileImage: File): Promise<void> => {
    const formData = new FormData();
    formData.append('image', newProfileImage);
  
    let res;
    try {
      res = await fetch(`${apiUrl}/users/profile/image`, {
        method: 'PATCH',
        headers: {
          Authorization: `Bearer ${cookies.token}`,
        },
        body: formData,
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
      
      const reader = new FileReader();
      reader.readAsDataURL(newProfileImage);
      reader.onloadend = () => {
        setProfilePicture(reader.result as string);
      };

      setSuccessMessage('Profile image updated');
      setSuccess(true);

    } catch(error: unknown) {
      if (error instanceof Error) {
        setFailMessage(error.message);
        setFail(true);
      } else {
        setFailMessage('An unknown error occurred');
        setFail(true);
      }
    }
  }

  const handleProfilePictureChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      updateProfileImage(file);
    }
  };

  const handleSave = async() => {
    if (newUsername) {
      await updateUsername();
    }
    if (newPassword) {
      await updatePassword();
    }
    setEditMode({ username: false, password: false });
  };

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
    <div className={`${s.profile} ${loading ? 'hidden' : 'fade-in-fast'}`}>
      <div className={s.profile_container}>
        <div className={s.profile_header}>
          <label htmlFor='profilePicture'>
            <img
              src={profilePicture || 'https://via.placeholder.com/80'}
              alt='Profile'
              className={s.profile_picture}
            />
          </label>
          <input
            id='profilePicture'
            type='file'
            accept='image/*'
            style={{ display: 'none' }}
            onChange={handleProfilePictureChange}
          />
          <div>
            <h2>{user?.username}</h2>
            <p>User profile.</p>
          </div>
        </div>

        <div className={s.info_container}>
          <div className={s.info_item}>
            <div>
              <div className={s.info_label}>Username</div>
              {editMode.username ? (
                <input
                  className={s.input}
                  type='text'
                  value={username}
                  onChange={(e) => {
                    setUsername(e.target.value);
                    setNewUsername(e.target.value);
                  }}
                />
              ) : (
                <div className={s.info_value}>{username}</div>
              )}
            </div>
            {editMode.username ? (
              <div className={s.button_group}>
                <button className={s.save_button} onClick={handleSave}>Save</button>
                <button
                  className={s.cancel_button}
                  onClick={() => setEditMode({ ...editMode, username: false })}
                >
                  Cancel
                </button>
              </div>
            ) : (
              // <button
              //   className={s.edit_button}
              //   onClick={() => setEditMode({ ...editMode, username: true })}
              // >
              //   Edit
              // </button>

              <Button variant='outlined' onClick={() => setEditMode({ ...editMode, username: true })}>
                Edit
              </Button>
            )}
          </div>

          <div className={s.info_item}>
            <div>
              <div className={s.info_label}>Password</div>
              {editMode.password ? (
                <input
                  className={s.input}
                  type='password'
                  placeholder='New Password'
                  value={newPassword || ''}
                  onChange={(e) => {
                    setPassword(e.target.value);
                    setNewPassword(e.target.value);
                  }}
                />
              ) : (
                <div className={s.info_value}>{password}</div>
              )}
            </div>
            {editMode.password ? (
              <div className={s.button_group}>
                <button className={s.save_button} onClick={handleSave}>Save</button>
                <button
                  className={s.cancel_button}
                  onClick={() => setEditMode({ ...editMode, password: false })}
                >
                  Cancel
                </button>
              </div>
            ) : (
              // <button
              //   className={s.edit_button}
              //   onClick={() => setEditMode({ ...editMode, password: true })}
              // >
              //   Edit
              // </button>
              <Button variant='outlined' onClick={() => setEditMode({ ...editMode, password: true })}>
                Edit
              </Button>
            )}
          </div>
        </div>
      </div>
      <div className={s.reviewContainer}>
        <h3 style={{textAlign: 'center', marginBottom: '1rem'}}>
          Your reviews
        </h3>
        {userReviews && userReviews.content ? (
          <div className={s.reviews}>
            {userReviews.content.map((review: Review, index: number) => (
              <div key={index}>
                <ReviewCard 
                  review={review} 
                  href={`/${review.type == 'movie' 
                    ? 'movies' 
                    : review.type == 'tv' 
                      ? 'shows' 
                      : 'anime'}/${review.mediaId}`
                  } 
                />
              </div>
            ))}
          </div>
          ) : (
            <div className={s.noReviews}>No reviews yet.</div>
        )}

        {userReviews && userReviews.start && userReviews.end && <Paging
          page={Number(searchParams.get('page')) || 1}
          setPage={(val: number) => {
            searchParams.set('page', val.toString());
            setSearchParams(searchParams);
          }}
          size={userReviews?.size}
          totalPages={userReviews?.totalPages}
          totalElements={userReviews?.totalElements}
          start={userReviews?.start}
          end={userReviews?.end}
          type={'reviews'}
          top={searchParams.get('page') ? 350 : 0}
        />}
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
    </div>
  );
};
