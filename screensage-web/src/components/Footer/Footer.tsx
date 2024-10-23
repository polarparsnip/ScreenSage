import { Link } from 'react-router-dom';
import { useUserContext } from '../../context';
import s from './Footer.module.scss'

export default function Footer() {
  const loginContext = useUserContext();
  const { user, login } = loginContext.userLoggedIn;

  return (
    <div className={s.footer}>
      <div>
        {login 
          ? `Logged in as ${user.username}`
          : <Link to={'/register'} >Create an account</Link>
        }
      </div>
    </div>
  )
};