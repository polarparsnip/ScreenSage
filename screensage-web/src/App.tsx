import { Routes, Route } from 'react-router-dom';
import { Layout } from './components/Layout/Layout';
import { AppWrapper } from './context';
import Home from './routes/home/Home';
import About from './routes/about/About';
import Login from './routes/login/Login';
import Register from './routes/register/Register';
import Media from './routes/media/Media';
import Profile from './routes/profile/Profile';
import MediaInfo from './routes/MediaInfo/MediaInfo';
import Challenge from './routes/challenge/Challenge';

function App() {
  return (
    <AppWrapper>
      <Layout>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/about' element={<About />} />
          <Route path='/login' element={<Login />} />
          <Route path='/register' element={<Register />} />
          <Route path='/challenge' element={<Challenge />} />
          <Route path='/profile' element={<Profile />} />
          <Route path='/movies' element={<Media type={'movies'} />} />
          <Route path='/shows' element={<Media type={'shows'} />} />
          <Route path='/anime' element={<Media type={'anime'} />} />
          <Route path='/movies/:id' element={<MediaInfo type={'movies'} />} />
          <Route path='/shows/:id' element={<MediaInfo type={'shows'} />} />
          <Route path='/anime/:id' element={<MediaInfo type={'anime'} />} />
        </Routes>
      </Layout>
    </AppWrapper>
  );
}

export default App
