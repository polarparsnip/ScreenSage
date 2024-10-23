import s from './Layout.module.scss';
import Footer from '../Footer/Footer';
import Header from '../Header/Header';

export function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className={s.layout}>
      <Header/>
      <main>
        {children}
      </main>
      <Footer/>
    </div>
  )
}
