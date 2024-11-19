/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './MediaFilter.module.scss';
import Dropdown from '../Dropdown/Dropdown';
import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';

export default function MediaFilter({ genres }: { genres: any }): React.JSX.Element {
  const [searchParams, setSearchParams] = useSearchParams();
  const [selectedGenre, setSelectedGenre] = useState<any>();

  // const searchTerm = searchParams.get('search') || '';
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState(searchTerm);
  
  // const setSearchTerm = (val: any) => {
  //   if (val && val.length > 0) {
  //     searchParams.set('search', val);
  //     searchParams.delete('page');
  //     setSearchParams(searchParams);
  //   } else {
  //     searchParams.delete('search');
  //     searchParams.delete('page');
  //     setSearchParams(searchParams);
  //   }
  // }

  useEffect(() => {
    const queryGenreId = searchParams.get('genre');
    if (queryGenreId) {
      const queryGenre = genres.find((genre: any) => genre.id == queryGenreId);
      if (queryGenre?.name) {
        setSelectedGenre(queryGenre.name);
      }
    }
  }, [genres, searchParams]);

  useEffect(() => {
    if (debouncedSearchTerm) {
      searchParams.set('search', debouncedSearchTerm);
      setSearchParams(searchParams);
    } else {
      searchParams.delete('search');
      setSearchParams(searchParams);
    }
  }, [debouncedSearchTerm, searchParams, setSearchParams]);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearchTerm(searchTerm);
    }, 500);

    return () => {
      clearTimeout(handler);
    };
  }, [searchTerm]);

  return (
    <div className={s.mediaFilter}>
      <div className={s.mobile_search}>
        <input
          type={'text'}
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
          }}
          placeholder={'Search ...'}
          className={s.searchInput}
        />
      </div>
      <div className={s.mediaFilter__dropdowns}>
        <Dropdown
          defaultValue={'Genre'}
          options={genres}
          selectedValue={selectedGenre}
          // onChange={setSelectedGenre}
          onChange={(val: any) => {
            setSelectedGenre(val.name)
            searchParams.set('genre', val.id);
            setSearchParams(searchParams);
          }}
        />
      </div>
      <div className={s.search}>
        <input
          type={'text'}
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
          }}
          placeholder={'Search ...'}
          className={s.searchInput}
        />
      </div>
    </div>
  );
};