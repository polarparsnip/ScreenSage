import s from './Register.module.scss'
import { useNavigate } from 'react-router-dom';
import { User } from '../../types';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@mui/material';

const apiUrl = import.meta.env.VITE_API_URL;

const registerHandler = async (event: React.FormEvent<HTMLFormElement>): Promise<User> => {
  event.preventDefault();
  const target = event.target as HTMLFormElement;

  const username = target.username.value;
  // const email = event.target.email.value;
  const password = target.password.value;
  const image = target.image.files[0];

  const formData = new FormData();
  formData.append('username', username);
  // formData.append('email', email);
  formData.append('password', password);
  if (image) {
    formData.append('image', image);
  }

  try {
    const res = await fetch(`${apiUrl}/register`, {
      method: 'POST',
      headers: {
        Accept: '*/*',  
        Connection: 'keep-alive',
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
        const message = await res.text();  // Handles plain text response
        console.error(message);
        throw new Error(message || 'Unknown error');
      }
    }
  
    const result = await res.json();
    return result;

  } catch(error: unknown) {
    if (error instanceof Error) {
      console.error('Error:', error.message)
      throw new Error(error.message);
    } else {
      throw new Error('An unknown error occurred');
    }
  }
};

export default function Register() {
  document.title = 'Register'
  const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);
  const [fileName, setFileName] = useState('Upload image');

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event?.target?.files) {
      const file = event.target.files[0];
      setFileName(file.name); 
    }
  };

  return (
    <>
      <div className={s.registerForm}>
        <h1>Register</h1>
        <form className={s.registerForm__form}
          onSubmit={async (event) => {
            event.preventDefault();

            try {
              const userInfo = await registerHandler(event);

              if (userInfo.username !== undefined) {
                navigate('/login');
              } 

            } catch(error: unknown) {
              if (error instanceof Error) {
                setError(error.message);
              } else {
                setError('An unknown error occurred');
              }
            }
          }}
        >
          <section className={s.registerForm__form__fields}>
            <label htmlFor='username'>Username</label>
            <input type='text' id='username' required/>
          </section>
          <section className={s.registerForm__form__fields}>
            <label htmlFor='password'>Password</label>
            <input type='password' id='password' required/>
          </section>
          <section className={`${s.registerForm__form__fields} ${s.fileUpload}`}>
            <label>Profile picture (optional):</label>
              <label className={s.buttonWrap} htmlFor='image'>
                {fileName}<input type='file' id='image' onChange={handleFileChange}/>
              </label>
          </section>
          <Button variant='outlined' type='submit'>Register</Button>
        </form>

        <section className={s.login}>
          <p>Already have an account?</p>
          <Link to='/login'>Log in</Link>
        </section>

        {error && <h3>{error}</h3>}
      </div>
    </>
  )
}