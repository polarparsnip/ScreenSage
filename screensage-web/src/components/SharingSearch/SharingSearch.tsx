import s from './SharingSearch.module.scss'
import { User } from '../../types';
import { useEffect, useRef, useState } from 'react';
import { useCookies } from 'react-cookie';
import { Button } from '@mui/material';
import Snackbar from '../Snackbar/Snackbar';

const apiUrl = import.meta.env.VITE_API_URL;

export default function SharingSearch({ listId, currentIds }: { listId: string, currentIds: number[] }) {
  const [cookies] = useCookies(['token']);

  const [fail, setFail] = useState<boolean>(false);
  const [failMessage, setFailMessage] = useState<string | null>(null);
  const [success, setSuccess] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [searchInput, setSearchInput] = useState<string>('');
  const [searchResults, setSearchResults] = useState<User[]>([]);
  const [selectedUserIds, setSelectedUserIds] = useState<number[]>([...currentIds]);
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchInput(e.target.value);
  };

  const handleClickOutside = (event: MouseEvent) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
      setSearchInput('')
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  // Debounced API call
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (searchInput.trim() === "") {
        setSearchResults([]);
        return;
      }

      fetch(`${apiUrl}/users/search?query=${encodeURIComponent(searchInput)}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${cookies.token}`,
        }
      }).then((res) => res.json())
      .then((data: User[]) => setSearchResults(data))
      .catch((err) => {
        console.error("Error fetching users:", err);
        setSearchResults([]); 
      });
      
    }, 300); // Debounce delay

    
    return () => clearTimeout(timeoutId);
  }, [cookies.token, searchInput]);
  

  const toggleSelectUser = (user: User) => {
    if (selectedUserIds.includes(user.id)) {
      // Deselect the user
      setSelectedUserIds(selectedUserIds.filter((id) => id !== user.id));
    } else {
      // Select the user
      setSelectedUserIds([...selectedUserIds, user.id]);
    }
  }

  const updateSharedWith = async (): Promise<void> => {
    try {
      const res = await fetch(`${apiUrl}/watchlists/${listId}/shared_with`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${cookies.token}`,
        },
        body: JSON.stringify({ sharedWith: [...selectedUserIds] })
      });

      if (res && !res.ok) {
        if (res.headers.get('content-type')?.includes('application/json')) {
          console.error('Error:', res.status, res.statusText);
          const message = await res.json();
          console.error(message.error);
          throw new Error(message.error || 'Unknown error');
        } else {
          const message = await res.text();  // Plain text response
          console.error(message);
          throw new Error(message || 'Unknown error');
        }
      }

      setSuccessMessage('List has been shared with selected users');
      setSuccess(true);

    } catch(error: unknown) {
      if (error instanceof Error) {
        console.error('Error:', error.message)
        setFailMessage(error.message);
        setFail(true);
      } else {
        setFailMessage('An unknown error occurred');
        setFail(true);
      }
    }
  }

  return (
    <div className={s.sharing}>
      <div className={s.sharing__search}>
        <input
          type="text"
          value={searchInput}
          onChange={handleSearchChange}
          placeholder="Search users..."
          className={s.sharing__search__input}
        />
        {searchResults.length > 0 && (
          <div className={s.dropdown} ref={dropdownRef}>
            {searchResults.map((user) => (
              <div
                key={user.username}
                className={`${s.dropdown__item} ${
                  selectedUserIds.includes(user.id) ? s.selected : ""
                }`}
                onClick={() => toggleSelectUser(user)}
              >
                {user.username}
              </div>
            ))}
          </div>
        )}
      </div>
      <Button variant='outlined' className={s.share_button} onClick={updateSharedWith}>
        Share
      </Button>

      <Snackbar
        type={'success'}
        open={success}
        setOpen={setSuccess}
        setMessage={setSuccessMessage}
      >
        {successMessage}
      </Snackbar>
      <Snackbar
        type={'error'}
        open={fail}
        setOpen={setFail}
        setMessage={setFailMessage}
      >
        {failMessage}
      </Snackbar>
    </div>
  );
};