/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './Login.module.scss'
import { useUserContext } from '../../context';
import { useNavigate } from 'react-router-dom';
import { UserInfo } from '../../types';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { Button } from '@mui/material';
import Snackbar from '../../components/Snackbar/Snackbar';

const apiUrl = import.meta.env.VITE_API_URL;


const loginHandler = async (event: React.FormEvent<HTMLFormElement>, setCookie: any): Promise<UserInfo> => {
  event.preventDefault();
  const target = event.target as HTMLFormElement;
  const username = target.username.value;
  const password = target.password.value;

  try {
    const res = await fetch(`${apiUrl}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: '*/*',
        'Accept-Encoding': 'gzip, deflate, br',
        Connection: 'keep-alive',
      },
      body: JSON.stringify({ username, password }),
    });

    if (res && !res.ok) {
      if (res.headers.get('content-type')?.includes('application/json')) {
        console.error('Error:', res.status, res.statusText);
        const message = await res.json();
        console.error(message.error);
        throw new Error(message.error || 'Unknown error');
      } else {
        const message = await res.text();  // Handles plain text response
        console.error(message);
        throw new Error(message || 'Unknown error');
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
  
    return result;

  } catch(error: unknown) {
    if (error instanceof Error) {
      console.error('Error:', error.message);
      throw new Error(error.message);
    } else {
      throw new Error('An unknown error occurred');
    }
  }
};

export default function Login() {
  document.title = 'Login';
  const navigate = useNavigate();
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_cookies, setCookie] = useCookies(['token', 'user']);
  const loginContext = useUserContext();

  const [fail, setFail] = useState<boolean>(false);
  const [failMessage, setFailMessage] = useState<string | null>(null);

  return (
    <>
      <div className={s.loginForm}>
        <h1>Log in</h1>
        <form className={s.loginForm__form}
          onSubmit={async (event) => {
            event.preventDefault();
              let userInfo;
              try {
                userInfo = await loginHandler(event, setCookie);
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
                  
              if (userInfo && userInfo.user !== undefined) {
                loginContext.setUserLoggedIn({ login: true, user: userInfo.user });
                // Cookies.set('user', JSON.stringify({ login: true, user: userInfo.user }), { expires: 3 });
                setCookie('user', JSON.stringify({ login: true, user: userInfo.user }), {
                  path: '/',
                  maxAge: 5 * 24 * 60 * 60,
                  secure: true,
                });

                navigate('/');          
              } 
          }}
        >
          <section className={s.loginForm__form__fields}>
            <label htmlFor='username'>Username</label>
            <input type='text' id='username' required/>
          </section>
          <section className={s.loginForm__form__fields}>
            <label htmlFor='password'>Password</label>
            <input type='password' id='password' required/>
          </section>
          <Button variant='outlined' type='submit'>Log in</Button>
        </form>

        <section className={s.loginForm__form__submit}>
          <p>New user?</p>
          <Link to='/register'>Create an account</Link>
        </section>
        <Snackbar
          type={'error'}
          open={fail}
          setOpen={setFail}
          setMessage={setFailMessage}
        >
          {failMessage}
        </Snackbar>
      </div>
    </>
  )
}