import { useCookies } from 'react-cookie';
import { Navigate, useNavigate } from 'react-router-dom';
import * as React from 'react';
import { createContext, useContext, useEffect, useState } from 'react';

interface IUserContext {
    userLoggedIn: { login: boolean; user: { id: number, username: string, watchlists: any, lists: any } };
    setUserLoggedIn: React.Dispatch<React.SetStateAction<{ login: boolean; 
      user: { id: number, username: string, watchlists: any, lists: any } }>>;
    logOut: () => void;
}
  
export const UserContext = createContext<IUserContext>({
    userLoggedIn: { login: false, user: { id: -1, username: '', watchlists: [], lists: [] } },
    setUserLoggedIn: () => {},
    logOut: () => {},
})


export function AppWrapper({ children }: { children: React.ReactNode }) {
  const navigate = useNavigate();
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [cookies, setCookie, removeCookie] = useCookies(['token', 'user']);

    const [userLoggedIn, setUserLoggedIn] = useState({
        login: false,
        user: { id: -1, username: '', watchlists: [], lists: [] },
    });

    // const [login, setLogin] = useState({});

    const logOut = () => {
      setUserLoggedIn({ login: false, user: { id: -1, username: '', watchlists: [], lists: [] }});
      removeCookie('token', { path: '/' });
      removeCookie('user', { path: '/' });
      navigate('/', { replace: true });
    }

    useEffect(() => {   
        const storedUser = cookies.user;

        if (storedUser) {
          setUserLoggedIn(storedUser);
        }
    }, []);

    const checkTokenExpiration = async () => {
      const token = cookies.token;
      if (token) {
        let tokenData;

        try {
          const tokenPayload = atob(token.split('.')[1]);
          tokenData = JSON.parse(tokenPayload);

          const currentTime = Math.floor(Date.now() / 1000);

          if (tokenData.exp && tokenData.exp < currentTime) {
            logOut();
          }

        } catch (error) {
            console.error('Error parsing token:', error);
        }
      }
    };

  useEffect(() => {
    checkTokenExpiration();
  });

    return (
        <UserContext.Provider value={{ logOut, userLoggedIn, setUserLoggedIn }}>
        { children }
        </UserContext.Provider>
    );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useUserContext() {
  return useContext(UserContext);
}