import s from './Challenge.module.scss';
import { useUserContext } from '../../context';
import { useEffect, useState } from 'react';
import { Button } from '@mui/material';
import { Challenge as ChallengeType, ChallengeOption } from '../../types';
import { Popup } from '../../components/Popup/Popup';
import { useNavigate } from 'react-router-dom';
import { Loader } from '../../components/Loader/Loader';
import { ErrorDisplay } from '../../components/ErrorDisplay/ErrorDisplay';
import Snackbar from '../../components/Snackbar/Snackbar';
import { useCookies } from 'react-cookie';

const apiUrl = import.meta.env.VITE_API_URL;


export default function Challenge() {
  document.title = 'Daily challenge';

  const loginContext = useUserContext();
  const { user } = loginContext.userLoggedIn;
  const [cookies] = useCookies(['token', 'user']);
  const navigate = useNavigate();

  const [challenge, setChallenge] = useState<ChallengeType | null>(null);
  const [selectedOption, setSelectedOption] = useState<ChallengeOption | null>(null);
  const [resultString, setResultString] = useState<string>('');
  const [result, setResult] = useState<boolean | null>(null);

  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [fail, setFail] = useState<boolean>(false);
  const [failMessage, setFailMessage] = useState<string | null>(null);

  const [isPopupVisible, setIsPopupVisible] = useState(false);

  const [hasCompleted, setHasCompleted] = useState<boolean>(false);

  const handleResult = () => {
    setIsPopupVisible(true);
  };

  const handleClosePopup = () => {
    setIsPopupVisible(false);
    navigate('/'); 
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(`${apiUrl}/challenge`, {
          method: 'GET',
          // headers: {
          //   'Content-Type': 'application/json',
          //   Authorization: `Bearer ${cookies.token}`,
          // }
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

        const challenge = await res.json();
        setChallenge(challenge);
        setLoading(false);

        if (challenge?.id && cookies.token) {
          const completedRes = await fetch(`${apiUrl}/challenge/${challenge.id}/has-completed`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${cookies.token}`,
            }
          });
  
          if (completedRes && !completedRes.ok) {
            if (completedRes.headers.get('content-type')?.includes('application/json')) {
              console.error('Error:', completedRes.status, completedRes.statusText);
              const message = await completedRes.json();
              console.error(message.error);
              throw new Error(message.error || 'Unknown error');
            } else {
              const message = await completedRes.text();  // Plain text response
              console.error(message);
              throw new Error(message || 'Unknown error');
            }
          }

          const challengeInfo = await completedRes.json();
          setHasCompleted(challengeInfo.completed)
        }

      } catch (error: unknown) {
        if (error instanceof Error) {
          setError(error.message);
          setLoading(false);
        } else {
          setError('An unknown error occurred');
          setLoading(false);
        }
      }
    };

    fetchData();
  }, []);

  const submitAnswer = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
    event.preventDefault();
    
    if (!selectedOption || !challenge?.id) {
      return;
    }

    let res;
    try {
      res = await fetch(`${apiUrl}/challenge/${challenge.id}?optionId=${selectedOption.id}&user=${user ? user.id : 0}`, {
        method: 'POST',
        // headers: {
        //   'Content-Type': 'application/json',
        //   Authorization: `Bearer ${cookies.token}`,
        // },
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
      const result = await res.json();

      setResult(result.answeredCorrectly);

      const resultString = `The correct answer was ${result.correctOption.option}. \n
                            Your answer was ${result.answeredCorrectly ? 'correct' : 'incorrect'}`;
      setResultString(resultString)
      handleResult();

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

  const handleOptionChange = (option: ChallengeOption) => {
    setSelectedOption(option);
  };

  if (loading) {
    return (
      <Loader isLoading={loading} />
    );
  }

  if (error) {
    return (
      <ErrorDisplay errorMessage={error} />
    );
  }

  if (hasCompleted) {
    return (
      <div className={s.completed}>
        <h1>You have already completed today's challenge</h1>
      </div>
    );
  }

  return (
    <div className={`${s.challengePage} ${loading ? 'hidden' : 'fade-in'}`}>
      <form className={s.form} onSubmit={submitAnswer}>
        {challenge?.image && <div className={s.form__image}>
          <img
            src={challenge.image}
            className={s.challenge_img}
            alt={'Challenge image'}
          />
        </div>}
        <div className={s.question}>{challenge?.question}</div>
        <div className={s.optionsGrid}>
          {challenge?.options.map((option: ChallengeOption, index: number) => (
            <label
              key={index}
              className={`${s.option} ${selectedOption === option ? s.selected : ''}`}
            >
              <input
                type='radio'
                name='answer'
                value={option.option}
                checked={selectedOption === option}
                onChange={() => handleOptionChange(option)}
                className={s.radioInput}
              />
              <span className={s.optionLabel}>{option.option}</span>
            </label>
          ))}
        </div>
        {/* <button type='submit' className={s.submitButton} disabled={!selectedOption}>
          Submit Answer
        </button> */}
        <Button 
          variant='outlined' 
          type='submit' 
          sx={{
            opacity: !selectedOption ? 0.5 : 1,
            pointerEvents: !selectedOption ? 'none' : 'auto',
          }}
        >
          Submit Answer
        </Button>
      </form>
      {isPopupVisible && resultString && (
        <Popup
          message={resultString}
          onClose={handleClosePopup}
          shadowColor={result ? 'green' : 'red'}
        />
      )}
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