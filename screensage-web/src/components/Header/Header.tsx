import { Link, NavLink } from 'react-router-dom'
import s from './Header.module.scss'
import { useEffect, useRef, useState } from 'react';
import { useUserContext } from '../../context';

export default function Header() {
  const [isOpen, setIsOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement | null>(null);
  const mediaMenuRef = useRef<HTMLDivElement | null>(null);
  const mediaRef = useRef<HTMLDivElement | null>(null);
  const accountMenuRef = useRef<HTMLDivElement | null>(null);
  const accountRef = useRef<HTMLDivElement | null>(null);
  const loginContext = useUserContext();
  const { login } = loginContext.userLoggedIn;

  const [isMediaOpen, setIsMediaOpen] = useState(false);
  const [isAccountOpen, setIsAccountOpen] = useState(false);

  const [scrollPosition, setScrollPosition] = useState<number>(0);
  const [showHeader, setShowHeader] = useState<boolean>(true);

  useEffect(() => {
    const handleScroll = () => {
      const currentScrollPos = window.scrollY;

      if (scrollPosition > currentScrollPos) {
        // Scrolling up, show header
        setShowHeader(true);
      } else {
        // Scrolling down, hide header
        setShowHeader(false);
      }

      setScrollPosition(currentScrollPos);
      setIsMediaOpen(false);
      setIsAccountOpen(false);
    };

    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [scrollPosition]);

  useEffect(() => {
    if (isOpen) {
      document.body.classList.add('no-scroll'); 
    } else {
      document.body.classList.remove('no-scroll');
    }
  }, [isOpen]);

  const toggleMediaDropdown = () => {
    setIsMediaOpen((prev) => !prev);
  };

  const toggleAccountDropdown = () => {
    setIsAccountOpen((prev) => !prev);
  };

  const links = [
    {
      id: 1,
      name: 'Home',
      href: '/',
    },
    {
      id: 2,
      name: 'About',
      href: '/about',
    },

    {
      id: 3,
      name: 'Log in',
      href: '/login',
    },
  ];

  const userLinks = [
    {
      id: 1,
      name: 'Home',
      href: '/',
    },
    {
      id: 2,
      name: 'Daily challenge',
      href: '/challenge',
    },
    {
      id:3,
      name: 'Scoreboard',
      href: '/scoreboard',
    },
    {
      id:4,
      name: 'Media',
      href: '/media',
    },
    {
      id:5,
      name: 'Account',
      href: '#',
    },
  ];

  const userLinksMobile = [
    {
      id: 1,
      name: 'Home',
      href: '/',
    },
    {
      id: 2,
      name: 'Daily challenge',
      href: '/challenge',
    },
    {
      id:3,
      name: 'Scoreboard',
      href: '/scoreboard',
    },
    {
      id:4,
      name: 'Movies',
      href: '/movies',
    },
    {
      id:5,
      name: 'Shows',
      href: '/shows',
    },
    {
      id:6,
      name: 'Anime',
      href: '/anime',
    },
    {
      id:7,
      name: 'Lists',
      href: '/lists',
    },
    {
      id:8,
      name: 'Profile',
      href: '/profile',
    },
    {
      id:9,
      name: 'Watchlists',
      href: '/watchlists',
    },
    {
      id:10,
      name: 'My lists',
      href: '/users/profile/lists',
    },
    {
      id:11,
      name: 'Log out',
      href: '#',
    },
  ];

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };


  const handleClickOutside = (event: MouseEvent) => {
    if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsOpen(false);
    }

    if (mediaRef.current && mediaRef.current.contains(event.target as Node)) {
      setTimeout(() => setIsMediaOpen(false), 200);
    }

    if (mediaMenuRef.current && !mediaMenuRef.current.contains(event.target as Node)) {
      setIsMediaOpen(false);
    }

    if (accountRef.current && accountRef.current.contains(event.target as Node)) {
      setTimeout(() => setIsAccountOpen(false), 200);
    }

    if (accountMenuRef.current && !accountMenuRef.current.contains(event.target as Node)) {
      setIsAccountOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);


  return (
    <div className={`${s.header}  ${!showHeader ? s.hidden : ''}`}>
      <div className={s.header__title}>
        <Link to={'/'} className={s.header__title}>
          <img src='/img/icon.png' alt='screensage-logo' width='60px' height='60px' />
          {/* <h1>ScreenSage</h1> */}
          <img src='/img/screenSageText.png' alt='screensage-text' width='200px' height='50px' />
        </Link>
      </div>
      <nav className={`${s.nav}`}>
        <ul className={s.nav__links}>
           {login 
            ? userLinks.map((link: { id: number, name: string, href: string }) => {
              if (link.id == 4) {
                return (
                <li key={link.id}>
                  <div className={s.dropdown} ref={mediaMenuRef}>
                    <Link to={'#'} className={s.dropdown__button} onClick={toggleMediaDropdown}>
                      Media
                    </Link>
                    {isMediaOpen && (
                      <div ref={mediaRef} className={`${s.dropdown__menu} ${isMediaOpen ? s.dropdown_menu__show : ''}`}>
                        <NavLink to={'/movies'}><div className={s.dropdown__menu__item}>Movies</div></NavLink>
                        <NavLink to={'/shows'}><div className={s.dropdown__menu__item}>Shows</div></NavLink>
                        <NavLink to={'/anime'}><div className={s.dropdown__menu__item}>Anime</div></NavLink>
                        <NavLink to={'/lists'}><div className={s.dropdown__menu__item}>Lists</div></NavLink>
                      </div>
                    )}
                  </div>
                </li>
                )
              } else if (link.id == 5) {
                return (
                  <li key={link.id}>
                    <div className={s.dropdown} ref={accountMenuRef}>
                      <Link to={'#'} className={s.dropdown__button} onClick={toggleAccountDropdown}>
                        Account
                      </Link>
                      {isAccountOpen && (
                        <div ref={accountRef} className={`${s.dropdown__menu} ${isAccountOpen ? s.dropdown_menu__show : ''}`}>
                          <NavLink to={'/profile'}><div className={s.dropdown__menu__item}>Profile</div></NavLink>
                          <NavLink to={'/watchlists'}><div className={s.dropdown__menu__item}>Watchlists</div></NavLink>
                          <NavLink to={'/users/profile/lists'}><div className={s.dropdown__menu__item}>My lists</div></NavLink>
                          <Link to={'#'} onClick={loginContext.logOut}><div className={s.dropdown__menu__item}>Log out</div></Link>
                        </div>
                      )}
                    </div>
                  </li>
                )
              } else {
                return (<li key={link.id}><NavLink to={link.href}>{link.name}</NavLink></li>);
              }
            })
            : links.map((link: { id: number, name: string, href: string }) => {
              return (<li key={link.id}><NavLink to={link.href}>{link.name}</NavLink></li>);
            })
           }
        </ul>

      </nav>
      <div className={s.mobile_menu} ref={menuRef}>
        <div className={
          `${s.mobile_menu__hamburger} ${isOpen ? s.mobile_menu__hamburger__open : ''}`
          } 
          onClick={toggleMenu}
        >
          {isOpen ? '✖' : '☰'}
        </div>

        <div className={`${s.mobile_menu__nav} ${isOpen ? s.mobile_menu__nav__open : ''}`}>
          <ul className={s.mobile_menu__nav__links}>
            {/* <li onClick={toggleMenu}><NavLink to='/'>Home</NavLink></li>
            <li onClick={toggleMenu}><NavLink to='/about'>About us</NavLink></li>
            <li onClick={toggleMenu}><NavLink to='/login'>Log in</NavLink></li> */}
            {login 
              ? userLinksMobile.map((link: { id: number, name: string, href: string }) => {
                return (<li key={link.id} onClick={toggleMenu}>
                  {link.id == 11 
                    ? <Link onClick={loginContext.logOut} to={link.href}>{link.name}</Link> 
                    : <NavLink to={link.href}>{link.name}</NavLink>}
                  </li>
                );
              })
              : links.map((link: { id: number, name: string, href: string }) => {
                return (<li key={link.id} onClick={toggleMenu}><NavLink to={link.href}>{link.name}</NavLink></li>);
              })
           }
          </ul>
        </div>
      </div>
    </div>
  )
};