import { useEffect } from 'react';
import s from './Loader.module.scss'
import { FadeLoader } from 'react-spinners';

export function Loader({ isLoading }: { isLoading: boolean }) {

  useEffect(() => {
    window.scrollTo({ top: 0 });
  }, []);

  return (
    <div className={s.loading}>
      <FadeLoader
        color={'var(--color-text-secondary)'}
        loading={isLoading}

        radius={'20px'}
        margin={25}
        height={30}
        width={10}
      />
    </div>
  );
}
