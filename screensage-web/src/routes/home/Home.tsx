import s from './Home.module.scss';
import { useUserContext } from '../../context';
import { Link } from 'react-router-dom';


export default function Home() {
  document.title = 'Home';

  const loginContext = useUserContext();
  const { login } = loginContext.userLoggedIn;

  return (
    <>
      <div className={s.home}>
        {login 
        ?
          <div className={s.home__main}>
            <img src='/img/logo.png' alt='' />
            <div className={s.home__main__buttons}>
              <Link to={'/challenge'}>Daily challenge</Link>
              <Link to={'/media'}>Media</Link>
            </div>
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
    </>
  )
};