/* eslint-disable @typescript-eslint/no-explicit-any */

import s from './Paging.module.scss';
import { useEffect } from 'react';

export default function Paging({
  page,
  setPage,
  size,
  totalPages,
  totalElements,
  start,
  end,
  type,
  top,
}: {
  page: any,
  setPage: any,
  size: number,
  totalPages: number,
  totalElements: number,
  start: number,
  end: number,
  type: string,
  top: number,
}): React.JSX.Element {

  useEffect(() => {
    setTimeout(() => window.scrollTo({ top: top }), 500);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  if (!totalElements || totalElements < size) {
    return <></>;
  }

  return (
    <section className={s.paging}>
      <hr className={s.paging__menuLine}></hr>
      <div className={s.paging__pages}>
        <h4>Showing {start} to {end} of {totalElements} {type}</h4>
        <div className={s.paging__change}>
          {page > 1 ? (
            <div className={s.paging__button} onClick={() => setPage(page - 1)} >
              Previous
            </div>
          ) : (
            null
          )}
          {page < totalPages ? (
            <div className={s.paging__button} onClick={() => setPage(page + 1)}>
              Next
            </div>
          ) : (
            null
          )}
        </div>
      </div>
    </section>
  );
}
