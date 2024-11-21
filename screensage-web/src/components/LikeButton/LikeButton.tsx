import s from './LikeButton.module.scss'
import { ThumbUp, ThumbUpOutlined } from '@mui/icons-material';

export default function LikeButton({  
  likeCount, 
  userHasLiked, 
  toggleLike 
}: {  
  likeCount: number;
  userHasLiked: boolean;
  toggleLike: () => Promise<void>;
}) {

  return (
    <button 
      className={`${s.likeButton} ${userHasLiked ? s.liked : ""}`} 
      onClick={toggleLike}
    >
      {userHasLiked ? (
        <ThumbUp className={s.icon} />
      ) : (
        <ThumbUpOutlined className={s.icon} />
      )}
      <span className={s.count}>{likeCount}</span>
    </button>
  );
};
