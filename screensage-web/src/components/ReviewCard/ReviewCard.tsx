import { Link } from 'react-router-dom'
import s from './ReviewCard.module.scss'
import { Rating } from '@mui/material'
import { Review } from '../../types'


export function ReviewCard({ review, href }: { review: Review, href?: string }) {
  return (
    <Link to={href ? href : `#`}>
      <div className={s.reviewCard}>
        {/* <div>
          {review.mediaId}
        </div> */}
        <div className={s.reviewCard__rating}>
          <span style={{fontWeight: 'normal'}}>Review by:</span> {review.user?.username ? review.user.username : 'Unknown'}
          <Rating
            value={review.rating}
            precision={0.5}
            readOnly
            style={{marginTop: '2px'}}
          />
        </div>

        <p>
          {review.content}
        </p>
      </div>
    </Link>
  )
}
