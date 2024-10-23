import s from './ErrorDisplay.module.scss'
import { useEffect } from 'react';

export function ErrorDisplay({ errorMessage }: { errorMessage: string }) {

  useEffect(() => {
    window.scrollTo({ top: 0 });
  }, []);

  return (
    <div className={s.errorDisplay}>
      {errorMessage}
    </div>
  );
}
